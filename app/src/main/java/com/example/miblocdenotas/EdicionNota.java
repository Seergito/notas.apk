package com.example.miblocdenotas;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EdicionNota extends Activity  {

	/* Variables para acceder al texto de la nota y botones */
	EditText etNota;
	Button bGrabar;
	Button bCancelar;

	TextView tv_titulo;
	int idnota;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edicion_nota);
		etNota	= findViewById(R.id.etNota);
		bGrabar = findViewById(R.id.bGrabar);
		bCancelar = findViewById(R.id.bCancelar);
		tv_titulo=findViewById(R.id.tvTituloMainActivity);

		// COMPLETAR. Si es una actualización debemos visualizar el texto de la nota y guarder su ID
		Intent recibido=getIntent();
		idnota=recibido.getIntExtra("idnota",-1);
		tv_titulo.setText("Insertando");
		if(idnota!=-1){
			etNota.setText(recibido.getStringExtra("texto"));
			tv_titulo.setText("Editando");
		}



		bGrabar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// COMPLETAR
				String nota=etNota.getText().toString();
				Intent respuesta=new Intent();
				respuesta.putExtra("id",idnota);
				respuesta.putExtra("nota",nota);
				setResult(RESULT_OK,respuesta);
				finish();
			}
		});

		bCancelar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// COMPLETAR
				setResult(RESULT_CANCELED);
				finish();
			}
		});

	}



}
