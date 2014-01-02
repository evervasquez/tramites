package com.mpsm.tramites;

import java.net.URLEncoder;
import java.util.ArrayList;

import librerias.ConstantsUtils;
import librerias.Pasos_model;
import librerias.ProgressFragment;
import librerias.WS;
import librerias.dialogos;
import librerias.verifica_internet;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import br.com.dina.ui.widget.UIButton;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.ClickListener;

public class Detalle_tramite extends ProgressFragment {
	View mContentView;
	WS objetoBD;
	SoapObject result;
	Intent intent;
	ArrayList<Pasos_model> datos_pasos;
	Pasos_model datos_p;
	ProgressDialog pd;
	public static final String TAG = "Detalle_tramite";
	String categoria, esta, datos;
	dialogos dialog;
	String content;
	String METHOD_NAME = "listarPasosMovil";
	String[] datos_tramite;
	String id_tramite;
	String tipo;

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
			id_tramite = datos_tramite[0];
			tipo = datos_tramite[7];
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

		UIButton.setTitle(datos_tramite[1]);
		UIButton.setSubTitle1("Código : " + datos_tramite[2]);
		UIButton.setSubTitle2("Solicitante : " + datos_tramite[4]);
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
			objetoBD = new WS(getSherlockActivity().getApplicationContext(),
					ConstantsUtils.CONTROLLER + METHOD_NAME, datos);

			// comprobamos si tenemos conexion a internet
			content = objetoBD.getResponse();
			return 1;
		}

		protected void onPreExecute() {
			try{
			setContentShown(false);
			String param = URLEncoder.encode("id_tramite", "UTF-8") + "="
					+ URLEncoder.encode(id_tramite, "UTF-8");
			param += "&" + URLEncoder.encode("tipo", "UTF-8") + "="
					+ URLEncoder.encode(tipo, "UTF-8");
			datos = param;

			Log.v(TAG, datos);
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
			
			JSONObject jsonResponse;
			jsonResponse = new JSONObject(content);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Andtroid");
			datos_pasos = new ArrayList<Pasos_model>();
			int lengthJsonArr = jsonMainNode.length();
			if (lengthJsonArr > 0) {
				Log.v("respuesta", content);
				for (int i = 0; i < lengthJsonArr; i++) {
					JSONObject jsonChildNode = jsonMainNode
							.getJSONObject(i);
					
					String pasos = (jsonChildNode.optString("descripcion_pasos").toString() == "null") ?
							"no disponible" : jsonChildNode.optString("descripcion_pasos").toString() ;
					String obs_origen = (jsonChildNode.optString("observaciones_origen").toString() == "null") ?
							"no disponible" : jsonChildNode.optString("observaciones_origen").toString() ;
					String obs_destino = (jsonChildNode.optString("observaciones_destino").toString() == "null") ?
							"no disponible" : jsonChildNode.optString("observaciones_destino").toString() ;
					String hora_origen = (jsonChildNode.optString("hora_origen").toString() == "null") ?
							"no disponible" : jsonChildNode.optString("hora_origen").toString() ;
					String usuario_origen = (jsonChildNode.optString("usuario_origen").toString() == "null") ?
							"no disponible" : jsonChildNode.optString("usuario_origen").toString() ;
					String oficina_origen = (jsonChildNode.optString("oficina_origen").toString() == "null") ?
							"no disponible" : jsonChildNode.optString("oficina_origen").toString() ;
					String oficina_origen_nom = (jsonChildNode.optString("oficina_origen_nom").toString() == "null") ?
							"no disponible" : jsonChildNode.optString("oficina_origen_nom").toString() ;
					String fecha_destino = (jsonChildNode.optString("fecha_destino").toString() == "null") ?
							"no disponible" : jsonChildNode.optString("fecha_destino").toString() ;
					String hora_destino = (jsonChildNode.optString("hora_destino").toString() == "null") ?
							"no disponible" : jsonChildNode.optString("hora_destino").toString() ;
					String usuario_destino = (jsonChildNode.optString("usuario_destino").toString() == "null") ?
							"no disponible" : jsonChildNode.optString("usuario_destino").toString() ;
					String oficina_destino = (jsonChildNode.optString("oficina_destino").toString() == "null") ?
							"no disponible" : jsonChildNode.optString("oficina_destino").toString() ;
					String oficina_destino_nom = (jsonChildNode.optString("oficina_destino_nom").toString() == "null") ?
							"no disponible" : jsonChildNode.optString("oficina_destino_nom").toString() ;
					String id_pasos = (jsonChildNode.optString("id_pasos").toString() == "null") ?
							"no disponible" : jsonChildNode.optString("id_pasos").toString() ;
					String id_cargo_destino = (jsonChildNode.optString("id_cargo_destino").toString() == "null") ?
									"no disponible" : jsonChildNode.optString("id_cargo_destino").toString() ;
					String estado = (jsonChildNode.optString("estado").toString() == "null") ?
							"no disponible" : jsonChildNode.optString("estado").toString() ;
					
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
					fecha_origen = jsonChildNode.optString("fecha_origen").toString();
					if (i == 0) {
						if (fecha_origen == "null") {
							fecha_origen = fecha_destino;
						} else {
							fecha_origen = fecha_origen
									.toString().trim();
						}
					} else {
						if (fecha_origen == "null") {
							fecha_origen = "Fecha Pendiente";
						} else {
							fecha_origen = fecha_origen
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
					tableView.addBasicItem(pasos,
							fecha_origen);
				}
				tableView.commit();
			}
	
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