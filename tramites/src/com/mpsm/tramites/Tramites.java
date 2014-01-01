package com.mpsm.tramites;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import librerias.ActivityProgress_detalle;
import librerias.ConstantsUtils;
import librerias.ProgressFragment;
import librerias.Tramite_model;
import librerias.UTF8;
import librerias.WS;
import librerias.dialogos;
import librerias.verifica_internet;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.ClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class Tramites extends ProgressFragment {
	// para el polltorefresh
	public int ESTADO_PULL = 0;
	private static final String TAG = "Tramites";
	private UITableView tableView = null;
	PullToRefreshScrollView mPullRefreshScrollView;
	ScrollView mScrollView;

	// fin
	View mContentView;
	public String cadena_buscada;
	WS objetoBD;
	private String content;
	Intent intent;
	ArrayList<Tramite_model> datos_tramite;
	Tramite_model datos_t;
	int limite_entero = 1, calculo, para_lista = 0;
	int factor = 10;
	String limite;
	ProgressDialog pd;
	String categoria, datos;
	dialogos dialog;
	private static final String METHOD_NAME = "listarTramitesMovil";
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
						if (tableView.getCount() > 0) {
							obtainData(tableView);
						} else {
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
		tableView = (UITableView) getView().findViewById(R.id.tableView);
		obtainData(tableView);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@SuppressWarnings("unchecked")
	private void obtainData(UITableView param) {
		calculo = limite_entero * factor;
		limite = "" + calculo;
		if (verifica_internet.checkConex(getSherlockActivity())) {
			tarea = new asyncTramites(cadena_buscada, limite, param);
			tarea.execute();

		} else {
			dialog = new dialogos();
			dialogos.Dialogo_Alerta(getSherlockActivity(),
					"nesecita estar conectado a internet");
			return;
		}
	}

	@SuppressWarnings("rawtypes")
	private class asyncTramites extends AsyncTask {
		private UITableView tableView1;
		private String codigo, limite;

		// long [] ids;
		public asyncTramites(String cod, String limit, UITableView param1) {
			// TODO Auto-generated constructor stub
			this.tableView1 = param1;
			this.codigo = cod;
			this.limite = limit;
		}

		protected void onPreExecute() {
			if (ESTADO_PULL != 1) {
				setContentShown(false);
				try {
					String param = URLEncoder.encode("parametro", "UTF-8")
							+ "=" + URLEncoder.encode(this.codigo, "UTF-8");
					param += "&" + URLEncoder.encode("limite", "UTF-8") + "="
							+ URLEncoder.encode(this.limite, "UTF-8");
					datos = param;

					Log.v(TAG, datos);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				setContentShown(true);
			}
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated mecodigothod stub

			objetoBD = new WS(getSherlockActivity().getApplicationContext(),
					ConstantsUtils.CONTROLLER + METHOD_NAME, datos);

			// comprobamos si tenemos conexion a internet
			content = objetoBD.getResponse();
			return 1;
		}

		protected void onPostExecute(Object res) {
			// Log.v("respuesta", content);
			try {

				// //////////////////////////////////
				
				JSONObject jsonResponse;

				if (ESTADO_PULL != 1) {
					CustomClickListener listener = new CustomClickListener();
					tableView1.setClickListener(listener);
				}
				setContentShown(true);
				jsonResponse = new JSONObject(content);

				// recuperamos el array
				JSONArray jsonMainNode = jsonResponse.optJSONArray("Andtroid");

				int lengthJsonArr = jsonMainNode.length();
				//id_tramite1 = new ArrayList<String>();
				tipo1 = new ArrayList<String>();
				if (lengthJsonArr > 0) {
					for (int i = 0; i < lengthJsonArr; i++) {
						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);
						id_tramite1.add(jsonChildNode.optString("id_tramite")
								.toString());
						tipo1.add(jsonChildNode.optString("tipo").toString());

						datos_t = new Tramite_model();

						datos_t.setId_tramite(id_tramite1.get(para_lista + i));// 0
						datos_t.setTramite(jsonChildNode.optString("tramite")
								.toString());// 1
						datos_t.setCodigo(jsonChildNode.optString("codigo")
								.toString());// 2
						datos_t.setFecha_inicio(jsonChildNode.optString(
								"fecha_inicio").toString());// 3
						datos_t.setSolicitante(jsonChildNode.optString(
								"solicitante").toString());// 4
						datos_t.setDni(jsonChildNode.optString("dni")
								.toString());// 5
						datos_t.setRuc(jsonChildNode.optString("ruc")
								.toString());// 6
						datos_t.setTipo(tipo1.get(para_lista + i));// 7
						String estado = jsonChildNode.optString("estado")
								.toString();
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

						datos_t.setEstado(estado);// 8
						datos_t.setNumero_folios(jsonChildNode.optString(
								"numero_folios").toString());// 9
						datos_t.setUsuario(jsonChildNode.optString("usuario")
								.toString());// 10

						datos_tramite.add(datos_t);

					}

				}

				if (ESTADO_PULL == 1) {
					tableView1.clear();
				}

				for (int i = 0; i < datos_tramite.size(); i++) {
					tableView1.addBasicItem(
							UTF8.convertirA_UTF8(datos_tramite.get(i)
									.getTramite()),
							"Código : "
									+ UTF8.convertirA_UTF8(datos_tramite.get(i)
											.getCodigo()) + "| fecha: "
									+ datos_tramite.get(i).getFecha_inicio(),
							"Solicitante: "
									+ UTF8.convertirA_UTF8(datos_tramite.get(i)
											.getSolicitante()), "DNI: "
									+ datos_tramite.get(i).getDni()
									+ " | RUC: "
									+ datos_tramite.get(i).getRuc(), "Estado: "
									+ datos_tramite.get(i).getEstado()
									+ " | Tipo: TRÁMITE EXTERNO");
				}

				tableView1.commit();
				mPullRefreshScrollView.onRefreshComplete();

				// //////////////////////////////////

				Log.v(TAG + " respuesta", content);
			} catch (Exception e) {
				/*
				 * Toast.makeText(getSherlockActivity(),
				 * "Hubo un error al descargar datos", Toast.LENGTH_SHORT)
				 * .show();
				 */

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