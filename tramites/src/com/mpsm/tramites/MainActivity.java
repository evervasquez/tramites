package com.mpsm.tramites;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import librerias.CustomTypeFace;
import librerias.ProgressActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;

public class MainActivity extends SherlockActivity {
	private Button iniciar;
	private EditText text_busca;
	String cadena_buscada;
	private static final String SHARED_FILE_NAME = "shared.png";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

			setTitle("Sistema de Trámite Móvil");
			
			int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
			if(titleId == 0)
			    titleId = com.actionbarsherlock.R.id.abs__action_bar_title;
			
			TextView custom = (TextView) findViewById(titleId);
			custom.setTypeface(CustomTypeFace.getInstance(this).getTypeFace());

			TextView titulo_bucar = (TextView) findViewById(R.id.natively_text_view);
			titulo_bucar.setTypeface(CustomTypeFace.getInstance(this).getTypeFace());
		
		text_busca = (EditText) findViewById(R.id.text_tramite);
		
		InputFilter[] filtros = new InputFilter[2];
		filtros[0] = new InputFilter.LengthFilter(50); // Solo escribo un
														// caracter
		filtros[1] = new InputFilter.AllCaps(); // Le obligo a que se convierta
												// en mayuscula
		text_busca.setFilters(filtros);
		// cadena_buscada = text_busca.getText().toString().trim();

		iniciar = (Button) findViewById(R.id.btn_buscar);
		iniciar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String cadena_buscada = text_busca.getText().toString().trim()
						.toUpperCase();
				if (cadena_buscada.length() > 0) {
					Intent intent = new Intent(MainActivity.this,
							ProgressActivity.class);
					intent.putExtra("cadena_buscada", cadena_buscada);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(),
							"Ingrese correctamente el dato a buscar",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		copyPrivateRawResuorceToPubliclyAccessibleFile();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate your menu.
		getSupportMenuInflater().inflate(R.menu.share_action_provider, menu);

		// Set file with share history to the provider and set the share intent.
		MenuItem actionItem = menu
				.findItem(R.id.menu_item_share_action_provider_action_bar);
		ShareActionProvider actionProvider = (ShareActionProvider) actionItem
				.getActionProvider();
		actionProvider
				.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		actionProvider.setShareIntent(createShareIntent());
		return true;
	}

	private Intent createShareIntent() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("image/*");
		Uri uri = Uri.fromFile(getFileStreamPath("shared.png"));
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		return shareIntent;
	}

	private void copyPrivateRawResuorceToPubliclyAccessibleFile() {
		InputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			inputStream = getResources().openRawResource(R.drawable.logo);// edite
			outputStream = openFileOutput(SHARED_FILE_NAME,
					Context.MODE_WORLD_READABLE);
			byte[] buffer = new byte[1024];
			int length = 0;
			try {
				while ((length = inputStream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, length);
				}
			} catch (IOException ioe) {
				/* ignore */
			}
		} catch (FileNotFoundException fnfe) {
			/* ignore */
		} finally {
			try {
				inputStream.close();
			} catch (IOException ioe) {
				/* ignore */
			}
			try {
				outputStream.close();
			} catch (IOException ioe) {
				/* ignore */
			}
		}
	}
}
