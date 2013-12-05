package com.mpsm.tramites;

import librerias.CustomTypeFace;
import librerias.UTF8;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;
import br.com.dina.ui.widget.UITableView;

import com.actionbarsherlock.app.SherlockActivity;

public class Detalle_pasos extends SherlockActivity {
	private String[] datos_pasos;
	private UITableView tableView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_pasos);
		
		Bundle args = getIntent().getExtras();
		if (args != null) {
			datos_pasos = args.getStringArray("datos_pasos");
		}
		setTitle(UTF8.convertirA_UTF8(datos_pasos[0]));
		
		//cambiando fuente en el actionbar
		int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		if(titleId == 0)
		    titleId = com.actionbarsherlock.R.id.abs__action_bar_title;
		
		TextView custom = (TextView) findViewById(titleId);
		custom.setTypeface(CustomTypeFace.getInstance(this).getTypeFace());
		
        tableView = (UITableView) findViewById(R.id.tableView);
        createList(datos_pasos);
        tableView.commitEver();
	}
	
	   private void createList(String[] datos) {
		   switch (datos[14].charAt(0)) {
			case 'T':datos[14] = "FINALIZADO";break;
			case 'R':datos[14] = "RECEPCIONADO";break;
			case 'P':datos[14] = "PENDIENTE";break;
			case 'D':datos[14] = "DERIVADO";break;
			case 'O':datos[14] = "OBSERVADO";break;
			case 'E':datos[14] = "TRÁMITE EXTERNO";break;
			default:break;
			}
		   
	    	tableView.setClickListener(null);
	    	tableView.addBasicItem("DATOS DE ENVIO", datos[1].toString()+" "+datos[2].toString(), UTF8.convertirA_UTF8(datos[3].toString()), UTF8.convertirA_UTF8(datos[5].toString()),datos[14].toString());
	    	tableView.addBasicItem("DATOS DE RECEPCIÓN", datos[7].toString()+" "+datos[8].toString(), UTF8.convertirA_UTF8(datos[9].toString()), UTF8.convertirA_UTF8(datos[11].toString()),datos[14].toString());
	  
	    }

}
