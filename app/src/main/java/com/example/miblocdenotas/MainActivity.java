package com.example.miblocdenotas;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;


public class MainActivity extends Activity {
	ListView listViewNotas;
	ImageButton imageButtonAnhadir;

	/* Constantes para lanzar la Activiy EdicionNota */	
	final int PETICION_EDIT=1;
	final int PETICION_INSERT=2;

	SimpleCursorAdapter sc_adaptador; // Cursor para el ListView
	BDNotas bdNotas; // SQLiteOpenHelper de la base de datos de las notas


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listViewNotas =  findViewById(R.id.listViewNotas);
		imageButtonAnhadir = findViewById(R.id.imageButtonAnhadir);
		registerForContextMenu(listViewNotas);

		bdNotas=new BDNotas(MainActivity.this);
		bdNotas.abreBD();
		String columnas[]={"NOTA_CORTADA","FECHA_CREACION_FORMATEADA"};
		int controles[]={R.id.tvTituloNota,R.id.tvFechaNota};

		sc_adaptador=new SimpleCursorAdapter(this,R.layout.item_lista_notas,bdNotas.getListadoNotas(),columnas,controles,0);

		listViewNotas.setAdapter(sc_adaptador);



		/* COMPLETAR. Obtener una instancia de BDNotas,
		* usando el método getListadoNotas,
		* crear el SimpleCursorAdapter y asignarlo a variable sc_adaptador y asignárselo al
		* ListView para visualizar las notas existentes en la base de datos
		*/		
		
		listViewNotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

				int num_fila = i;

				Intent it=new Intent(MainActivity.this,EdicionNota.class);
				Cursor c = sc_adaptador.getCursor();
				c.moveToPosition(num_fila);

				it.putExtra("idnota",c.getInt(1));
				it.putExtra("texto",c.getString(2));
				startActivityForResult(it,PETICION_EDIT);

				/* COMPLETAR
				 * lanzar la Activity EdicionNota utilizando la constante PETICION_EDIT
				 * los datos que le enviaremos sera el texto de la nota e ID_NOTA
				 */
			}
		});
		imageButtonAnhadir.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				/* COMPLETAR.
		    		 lanzar la Activity EdicionNota utilizando la constante PETICION_INSERT
				*/
				Intent it=new Intent(MainActivity.this,EdicionNota.class);
				startActivityForResult(it,PETICION_INSERT);


			}
		});


	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// COMPLETAR
		if(requestCode==PETICION_EDIT && resultCode==RESULT_CANCELED){
			Toast.makeText(this, "EDICIÓN CANCELADA", Toast.LENGTH_SHORT).show();
		}
		if(requestCode==PETICION_EDIT && resultCode==RESULT_OK){
			Toast.makeText(this, "EDICIÓN REALIZADA", Toast.LENGTH_SHORT).show();
			int id=data.getIntExtra("id",-1);
			String nota=data.getStringExtra("nota");
			bdNotas.actualizarNota(id,nota);
			sc_adaptador.changeCursor(bdNotas.getListadoNotas());
		}

		if(requestCode==PETICION_INSERT && resultCode==RESULT_CANCELED){
			Toast.makeText(this, "INSERCIÓN CANCELADA", Toast.LENGTH_SHORT).show();
		}
		
		if(requestCode==PETICION_INSERT && resultCode==RESULT_OK){ //YAS
			bdNotas.insertarNota(data.getStringExtra("nota"));
			Toast.makeText(this, data.getStringExtra("Nota Insertada"), Toast.LENGTH_SHORT).show();
			sc_adaptador.changeCursor(bdNotas.getListadoNotas());
		}

	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.listViewNotas) {
			getMenuInflater().inflate(R.menu.menu_contextual_listview, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(@NonNull MenuItem item) {
		/* Obtenemos el rowid (_id) del cursor con la información
		   de contexto que nos da el item de menú pulsado, que
		   convertimos en información de adaptador
		   AdapterContextMenuInfo
		*/
		if (item.getItemId()== R.id.menu_contextual_listvew_borrar_nota){

			AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
			adb.setTitle("ATENCION");

			adb.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
					long rowid = info.id;
					bdNotas.borrarNota(rowid);
					sc_adaptador.changeCursor(bdNotas.getListadoNotas());
				}
			});
			adb.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});
			adb.create().show();

			// COMPLETAR. Aquí debemos borrar la nota con este rowid

		}

		if(item.getItemId()== R.id.id_compartir){
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
			long rowid = info.id;
			Intent it = new Intent(Intent.ACTION_SEND);
			it.setType("text/plain");
			String nota=bdNotas.compartir(rowid);
			it.putExtra(Intent.EXTRA_TEXT,nota);
			startActivity(Intent.createChooser(it,"nota"));
		}

		return super.onContextItemSelected(item);
	}

}
