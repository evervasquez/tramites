package com.mpsm.tramites;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import librerias.ActivityProgress_detalle;
import librerias.ConstantsUtils;
import librerias.ProgressFragment;
import librerias.Tramite_model;
import librerias.WS;
import librerias.dialogos;
import librerias.verifica_internet;

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
	public UITableView tableView = null;
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
	String categoria,datos;
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
		if (verifica_internet.checkConex(getSherlockActivity())) {
			tarea = new asyncTramites(cadena_buscada,limite,param);
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
		public asyncTramites(String cod,String limit,UITableView param1) {
			// TODO Auto-generated constructor stub
			this.tableView1 = param1;
			this.codigo = cod;
			this.limite = limit;
		}
		
		protected void onPreExecute() {
			if (ESTADO_PULL != 1) {
				//setContentShown(false);
				try {
					String param = URLEncoder.encode("codigo", "UTF-8") + "="
							+ URLEncoder.encode(this.codigo, "UTF-8");
					param += "&" + URLEncoder.encode("limite", "UTF-8") + "="
							+ URLEncoder.encode(this.limite, "UTF-8");
					datos = param;
				} catch(UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else {
				//setContentShown(true);
			}
		}
		
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			
			objetoBD = new WS(getSherlockActivity().getApplicationContext(),ConstantsUtils.CONTROLLER+METHOD_NAME,datos);

			// comprobamos si tenemos conexion a internet
			content = objetoBD.getResponse();
			return 1;
		}

		

		protected void onPostExecute(Object res) {
			try {
				String OutputData = "";
				JSONObject jsonResponse;
				ArrayList<String> datosUsusario;

				pd.dismiss();

				Log.v(TAG, content);
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