package com.mpsm.tramites;

import java.util.ArrayList;
import java.util.Vector;

import librerias.Pasos_model;
import librerias.ProgressFragment;
import librerias.UTF8;
import librerias.conecta_ws;
import librerias.dialogos;
import librerias.verifica_internet;

import org.ksoap2.serialization.SoapObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import br.com.dina.ui.widget.UIButton;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.ClickListener;

public class Detalle_tramite extends ProgressFragment {
	View mContentView;
	conecta_ws objetoBD;
	SoapObject result;
	Intent intent;
	String limite = "10";
	ArrayList<Pasos_model> datos_pasos;
	Pasos_model datos_p;
	ProgressDialog pd;
	String categoria, esta;
	dialogos dialog;
	String METHOD_NAME = "ver_tramite";
	String[] datos_tramite;
	String[] nom_variables;
	String[] datos_variables;

	// public String[] id_tramite, tipo;
	public static Detalle_tramite newInstance() {
		Detalle_tramite fragment = new Detalle_tramite();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle args = getArguments();
		if (args != null) {
			datos_tramite = args.getStringArray("datos_tramite");
		}
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContentView = inflater
				.inflate(R.layout.activity_detalle_tramite, null);
		// Toast.makeText(getSherlockActivity(), ""+datos_tramite[1],
		// Toast.LENGTH_SHORT).show();
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setContentView(mContentView);

		UIButton.setTitle(UTF8.convertirA_UTF8(datos_tramite[1]));
		UIButton.setSubTitle1("Código : " + datos_tramite[2]);
		UIButton.setSubTitle2("Solicitante : " + UTF8.convertirA_UTF8(datos_tramite[4]));
		UIButton.setSubTitle3("Fecha : " + datos_tramite[3]+" N° Folio : "+datos_tramite[9]);
		UIButton.setSubTitle4("Estado de Tramite : " + datos_tramite[8]);
		// UIButton.setImage(//image)
		obtainData();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@SuppressWarnings("unchecked")
	private void obtainData() {
		nom_variables = new String[] { "tramite", "tipo" };// nombres de las //
															// variables
		datos_variables = new String[] { datos_tramite[0], datos_tramite[7] };// datos
																				// de
																				// las
		if (verifica_internet.checkConex(getSherlockActivity())) {
			asyncTramites tarea = new asyncTramites();
			tarea.execute();
		} else {
			dialog = new dialogos();
			dialog.Dialogo_Alerta(getSherlockActivity(),
					"nesecita estar conectado a internet");
			return;
		}
	}

	@SuppressWarnings("rawtypes")
	private class asyncTramites extends AsyncTask {
		UITableView tableView;
		SoapObject transaction0;

		// long [] ids;
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			objetoBD = new conecta_ws();
			// comprobamos si tenemos conexion a internet
			result = (SoapObject) objetoBD.get_ResultadoWS(
					getSherlockActivity(), nom_variables, datos_variables,
					METHOD_NAME);
			return 1;
		}

		protected void onPreExecute() {
			try{
			setContentShown(false);
			}catch(Exception e){
				getSherlockActivity().finish();
			}
		}

		protected void onPostExecute(Object res) {
	try {
			tableView = (UITableView) getView().findViewById(R.id.tableView);
			CustomClickListener listener = new CustomClickListener();
			tableView.setClickListener(listener);
			setContentShown(true);
			Vector<?> transactions = (Vector<?>) result
					.getProperty("pasos_list");

			datos_pasos = new ArrayList<Pasos_model>();
			for (int i = 0; i < transactions.size(); i++) {

				transaction0 = (SoapObject) transactions.elementAt(i);// recupero
																		// el
																		// primer
																		// array
				String pasos = transaction0.getProperty("descripcion_pasos")
						.toString().trim();
				String obs_origen = transaction0
						.getProperty("observaciones_origen").toString().trim();
				String obs_destino = transaction0
						.getProperty("observaciones_destino").toString().trim();

				String hora_origen;
				if (transaction0.getProperty("hora_origen") == null) {
					hora_origen = "no disponible";
				} else {
					hora_origen = transaction0.getProperty("hora_origen")
							.toString();
				}
				String usuario_origen;
				if (transaction0.getProperty("usuario_origen") == null) {
					usuario_origen = "no disponible";
				} else {
					usuario_origen = transaction0.getProperty("usuario_origen")
							.toString().trim();
				}
				String oficina_origen;
				if (transaction0.getProperty("oficina_origen") == null) {
					oficina_origen = "no disponible";
				} else {
					oficina_origen = transaction0.getProperty("oficina_origen")
							.toString().trim();
				}
				String oficina_origen_nom;
				if (transaction0.getProperty("oficina_origen_nom") == null) {
					oficina_origen_nom = "no disponible";
				} else {
					oficina_origen_nom = transaction0
							.getProperty("oficina_origen_nom").toString()
							.trim();
				}
				String fecha_destino;
				if (transaction0.getProperty("fecha_destino") == null) {
					fecha_destino = "Fecha Pendiente";
				} else {
					fecha_destino = transaction0.getProperty("fecha_destino")
							.toString();
				}
				// String fecha_destino =
				// transaction0.getProperty("fecha_destino").toString().trim();
				String hora_destino;
				if (transaction0.getProperty("hora_destino") == null) {
					hora_destino = "no disponible";
				} else {
					hora_destino = transaction0.getProperty("hora_destino")
							.toString();
				}
				String usuario_destino;
				if (transaction0.getProperty("usuario_destino") == null) {
					usuario_destino = "no disponible";
				} else {
					usuario_destino = transaction0.getProperty(
							"usuario_destino").toString();
				}
				String oficina_destino;
				if (transaction0.getProperty("oficina_destino") == null) {
					oficina_destino = "no disponible";
				} else {
					oficina_destino = transaction0.getProperty(
							"oficina_destino").toString();
				}
				String oficina_destino_nom;
				if (transaction0.getProperty("oficina_destino_nom") == null) {
					oficina_destino_nom = "no disponible";
				} else {
					oficina_destino_nom = transaction0.getProperty(
							"oficina_destino_nom").toString();
				}
				String estado = transaction0.getProperty("estado").toString()
						.trim();
				String id_pasos = transaction0.getProperty("id_pasos")
						.toString().trim();
				String id_cargo_destino = transaction0
						.getProperty("id_cargo_destino").toString().trim();

				switch (estado.charAt(0)) {
				case 'T':esta = "FINALIZADO";break;
				case 'R':esta = "RECEPCIONADO";break;
				case 'P':esta = "PENDIENTE";break;
				case 'E':esta = "TRÁMITE EXTERNO";break;
				case 'O':esta = "OBSERVADO";break;
				case 'A':esta = "EN PROCESO";break;
				default:break;
				}
				
				if (pasos == "") {
					pasos = "Paso " + (i + 1);
				}
				// lleno al listveiw
				String fecha_origen = null;
				if (i == 0) {
					if (transaction0.getProperty("fecha_origen") == null) {
						fecha_origen = fecha_destino;
					} else {
						fecha_origen = transaction0.getProperty("fecha_origen")
								.toString().trim();
					}
				} else {
					if (transaction0.getProperty("fecha_origen") == null) {
						fecha_origen = "Fecha Pendiente";
					} else {
						fecha_origen = transaction0.getProperty("fecha_origen")
								.toString().trim();
					}
				}
				
				datos_p = new Pasos_model();
				datos_p.setPaso(pasos);//0
				datos_p.setObs_origen(obs_origen);//1
				datos_p.setObs_destino(obs_destino);//2
				datos_p.setHora_origen(hora_origen);//3
				datos_p.setUsuario_origen(usuario_origen);//4
				datos_p.setOficina_origen(oficina_origen);//5
				datos_p.setOficina_origen_nom(oficina_origen_nom);//6
				datos_p.setFecha_destino(fecha_destino);//7
				datos_p.setHora_destino(hora_destino);//8
				datos_p.setUsuario_destino(usuario_destino);//9
				datos_p.setOficina_destino(oficina_destino);//10
				datos_p.setOficina_destino_nom(oficina_destino_nom);//11
				datos_p.setEstado(estado);//12
				datos_p.setId_pasos(id_pasos);//13
				datos_p.setId_cargo_destino(id_cargo_destino);//14
				datos_p.setFecha_origen(fecha_origen);//15
				
				datos_pasos.add(datos_p);
				tableView.addBasicItem(UTF8.convertirA_UTF8(pasos),
						fecha_origen);
			}

			tableView.commit();
	}catch(Exception e){
		//Toast.makeText(getSherlockActivity(), "La acción ha sido cancelada", Toast.LENGTH_SHORT).show();
	}

		}

		private class CustomClickListener implements ClickListener {

			@Override
			public void onClick(int index) {
				if(datos_pasos.get(index).getFecha_origen()!="Fecha Pendiente"){
				intent = new Intent(getSherlockActivity(),Detalle_pasos.class);
				String[] data3 = new String[]{
						  datos_pasos.get(index).getPaso(),//0
						  datos_pasos.get(index).getFecha_origen(),//1
						  datos_pasos.get(index).getHora_origen(),//2
						  datos_pasos.get(index).getUsuario_origen(),//3
						  datos_pasos.get(index).getOficina_origen(),//4
						  datos_pasos.get(index).getOficina_origen_nom(),//5
						  datos_pasos.get(index).getObs_origen(),//6
						  datos_pasos.get(index).getFecha_destino(),//7
						  datos_pasos.get(index).getHora_destino(),//8
						  datos_pasos.get(index).getUsuario_destino(),//9
						  datos_pasos.get(index).getOficina_destino(),//10
						  datos_pasos.get(index).getOficina_destino_nom(),//11
						  datos_pasos.get(index).getObs_destino(),//12
						  datos_pasos.get(index).getId_cargo_destino(),//13
						  datos_pasos.get(index).getEstado()//14
						  	};
				intent.putExtra("datos_pasos", data3);
				startActivity(intent);
				}else{
					Toast.makeText(getSherlockActivity(), "Paso No disponible", Toast.LENGTH_SHORT).show();
				}
			}

		}

	}

}