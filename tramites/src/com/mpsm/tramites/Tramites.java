package com.mpsm.tramites;

import java.util.ArrayList;
import java.util.Vector;

import librerias.ActivityProgress_detalle;
import librerias.ProgressFragment;
import librerias.Tramite_model;
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
import android.widget.ScrollView;
import android.widget.Toast;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.ClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class Tramites extends ProgressFragment {
	// para el polltorefresh
	public int ESTADO_PULL = 0;
	public UITableView tableView = null;
	PullToRefreshScrollView mPullRefreshScrollView;
	ScrollView mScrollView;

	// fin
	View mContentView;
	String cadena_buscada;
	conecta_ws objetoBD;
	SoapObject result;
	Intent intent;
	ArrayList<Tramite_model> datos_tramite;
	Tramite_model datos_t;
	int limite_entero = 1, calculo, para_lista = 0;
	int factor = 10;
	String limite;
	ProgressDialog pd;
	String categoria;
	dialogos dialog;
	String METHOD_NAME = "listar_tramites";
	String[] nom_variables;
	String[] datos_variables;
	asyncTramites tarea;
	public String[] id_tramite, tipo;
	public ArrayList<String> id_tramite1, tipo1;

	public static Tramites newInstance() {
		Tramites fragment = new Tramites();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle args = getArguments();
		if (args != null) {
			cadena_buscada = args.getString("cadena_buscada");
		}
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.lista_tramites, null);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setContentView(mContentView);
		// metodo para el polltorefresh
		mPullRefreshScrollView = (PullToRefreshScrollView) mContentView
				.findViewById(R.id.pull_refresh_scrollview);

		mPullRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						// new GetDataTask().execute();
						limite_entero = limite_entero + 1;
						para_lista = para_lista + 1;
						ESTADO_PULL = 1;
						if(tableView.getCount()>0){
							obtainData(tableView);	
						}else{
						tableView = (UITableView) getView().findViewById(
									R.id.tableView);
						obtainData(tableView);
						}

					}
				});

		mPullRefreshScrollView.getRefreshableView();

		// fin
		id_tramite1 = new ArrayList<String>();
		tipo1 = new ArrayList<String>();
		datos_tramite = new ArrayList<Tramite_model>();
		tableView = (UITableView) getView().findViewById(
				R.id.tableView);
		obtainData(tableView);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@SuppressWarnings("unchecked")
	private void obtainData( UITableView param) {
		calculo = limite_entero * factor;
		limite = "" + calculo;
		// Toast.makeText(getSherlockActivity(), limite,
		// Toast.LENGTH_SHORT).show();
		nom_variables = new String[] { "codigo", "limite" };// nombres de las //
															// variables
		datos_variables = new String[] { cadena_buscada, limite };// datos de
																	// las
		if (verifica_internet.checkConex(getSherlockActivity())) {
			tarea = new asyncTramites(param);
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
		private UITableView tableView1;
		SoapObject transaction0;

		// long [] ids;
		public asyncTramites(UITableView param1) {
			// TODO Auto-generated constructor stub
			tableView1 = param1;
		}
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
			if (ESTADO_PULL != 1) {
				setContentShown(false);
			} else {
				setContentShown(true);
			}
		}

		protected void onPostExecute(Object res) {
			try {
				if(ESTADO_PULL!=1){
				CustomClickListener listener = new CustomClickListener();
				tableView1.setClickListener(listener);
				}
				setContentShown(true);
				Vector<?> transactions = (Vector<?>) result
						.getProperty("tramite_list");
				
				if(transactions.size()>0){
				for (int i = 0; i < transactions.size(); i++) {
					transaction0 = (SoapObject) transactions.elementAt(i);// recupero
																			// el
																			// primer
																			// array

					id_tramite1.add(transaction0.getProperty("id_tramite")
							.toString().trim());
					String tramite = transaction0.getProperty("tramite")
							.toString().trim();
					String codigo = transaction0.getProperty("codigo")
							.toString().trim();
					String fecha_inicio = transaction0
							.getProperty("fecha_inicio").toString().trim();
					String solicitante = transaction0
							.getProperty("solicitante").toString().trim();
					String dni = transaction0.getProperty("dni").toString()
							.trim();
					String ruc = transaction0.getProperty("ruc").toString()
							.trim();
					tipo1.add(transaction0.getProperty("tipo").toString()
							.trim());
					
					// tipo[i] =
					// transaction0.getProperty("tipo").toString().trim();
					String estado = transaction0.getProperty("estado")
							.toString().trim();
					String numero_folios = transaction0
							.getProperty("numero_folios").toString().trim();
					String usuario = transaction0.getProperty("usuario")
							.toString().trim();

					switch (estado.charAt(0)) {
					case 'T':
						estado = "FINALIZADO";
						break;
					case 'O':
						estado = "OBSERVADO";
						break;
					case 'A':
						estado = "EN PROCESO";
						break;
					default:
						break;
					}

					datos_t = new Tramite_model();
					datos_t.setId_tramite(id_tramite1.get(para_lista + i));// 0
					datos_t.setTramite(tramite);// 1
					datos_t.setCodigo(codigo);// 2
					datos_t.setFecha_inicio(fecha_inicio);// 3
					datos_t.setSolicitante(solicitante);// 4
					datos_t.setDni(dni);// 5
					datos_t.setRuc(ruc);// 6
					datos_t.setTipo(tipo1.get(para_lista + i));// 7
					datos_t.setEstado(estado);// 8
					datos_t.setNumero_folios(numero_folios);// 9
					datos_t.setUsuario(usuario);// 10

					datos_tramite.add(datos_t);
					// lleno al listveiw

				}
				if(ESTADO_PULL==1){
					tableView1.clear();	
				}
				for( int i = 0 ; i < datos_tramite.size() ; i++ ){
					tableView1
					.addBasicItem(UTF8.convertirA_UTF8(datos_tramite.get(i).getTramite()),
							"Código : " + UTF8.convertirA_UTF8(datos_tramite.get(i).getCodigo())+ 
							"| fecha: " + datos_tramite.get(i).getFecha_inicio(), "Solicitante: "
							+ UTF8.convertirA_UTF8(datos_tramite.get(i).getSolicitante()),
							"DNI: " + datos_tramite.get(i).getDni() + " | RUC: " + datos_tramite.get(i).getRuc(),
							"Estado: " + datos_tramite.get(i).getEstado()
							+ " | Tipo: TRÁMITE EXTERNO");
					}

				tableView1.commit();
				mPullRefreshScrollView.onRefreshComplete();
				// fin
				}else{
				Toast.makeText(getSherlockActivity(), "No se econtraron coincidencias", Toast.LENGTH_SHORT).show();	
				}
			} catch (Exception e) {
				/*Toast.makeText(getSherlockActivity(),
						"Hubo un error al descargar datos", Toast.LENGTH_SHORT)
						.show();*/

			}
		}

		private class CustomClickListener implements ClickListener {
			@Override
			public void onClick(int index) {
				intent = new Intent(getSherlockActivity(),
						ActivityProgress_detalle.class);
				String[] data2 = new String[] {
						datos_tramite.get(index).getId_tramite(),
						datos_tramite.get(index).getTramite(),
						datos_tramite.get(index).getCodigo(),
						datos_tramite.get(index).getFecha_inicio(),
						datos_tramite.get(index).getSolicitante(),
						datos_tramite.get(index).getDni(),
						datos_tramite.get(index).getRuc(),
						datos_tramite.get(index).getTipo(),
						datos_tramite.get(index).getEstado(),
						datos_tramite.get(index).getNumero_folios(),
						datos_tramite.get(index).getUsuario()

				};
				intent.putExtra("datos_tramite", data2);
				startActivity(intent);
			}

		}
	}
}