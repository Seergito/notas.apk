package com.example.miblocdenotas;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class BDNotas extends SQLiteOpenHelper {
    final static String FICH_BD="notas.sqlite";
    final static int VERSION=2;

    SQLiteDatabase db=null;
    Context contexto;
    public BDNotas(@Nullable Context context) {
        super(context, FICH_BD, null, VERSION);
        contexto = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql;
        sql ="CREATE TABLE TABLA_NOTAS ( " +
                " ID_NOTA INTEGER PRIMARY KEY ASC AUTOINCREMENT," +
                " NOTA TEXT NOT NULL," +
                " FECHA_CREACION DATETIME NOT NULL )";

        sqLiteDatabase.execSQL(sql);

        onUpgrade(sqLiteDatabase,1, VERSION);
        // COMPLETAR

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        if(i==1 && i1>=2){
            String sql="ALTER TABLE TABLA_NOTAS ADD COLUMN FECHA_MODIFICACION DATETIME";
            sqLiteDatabase.execSQL(sql);
            i1=2;
        }

    }

    public void abreBD() {
        if (db==null) {
            db = this.getReadableDatabase();
        }
    }

    public  void cierraBD() {
        if (db!=null) {
            db.close();
            db = null;
        }
    }

    /* Metodo para obtener las notas de la base de datos */
    public Cursor getListadoNotas(){
        abreBD();
        Cursor c = db.rawQuery("SELECT rowid as _id, ID_NOTA, NOTA,substr(NOTA,1,15) AS NOTA_CORTADA, FECHA_CREACION, strftime('%d/%m/%Y %H:%M:%S',FECHA_CREACION) AS FECHA_CREACION_FORMATEADA FROM TABLA_NOTAS order by ID_NOTA DESC",null);
            /*  Las columnas que obtenemos son:
             *  _id : Identificadador de fila para el SimpleCursorAdapter
             *  ID_NOTA: Valor clave entero
             *  NOTA: El texto de la nota
             *  NOTA_CORTADA: Los primeros 15 caracteres de la nota
             *  FECHA_CREACION: Fecha en que se creo la nota
             *  FECHA_CREACION_FORMATEADA: FECHA_CREACION formateada en dia/mes/anho hora:minuto:segundos
             */
        return c;

    }

    /* Metodo para obtener un String con la fecha y hora actuales
     * en formato correcto de base de datos: anho-mes-dia hora:minuto:segundos*/
    public static String getStringFechaHoraActual() {
        GregorianCalendar calendarioHoy= new GregorianCalendar();
        Date fechaHoraActual = calendarioHoy.getTime();

        SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return formateador.format(fechaHoraActual);
    }

    public long insertarNota(String nota) {
        abreBD();
        // COMPLETAR
      /*  String sql="INSERT INTO TABLA_NOTAS (NOTA,FECHA_CREACION) VALUES(?,?)";
        String campos[]={nota,getStringFechaHoraActual()};
        db.execSQL(sql,campos);
    */
        ContentValues valores=new ContentValues();
        valores.put("NOTA",nota);
        valores.put("FECHA_CREACION",getStringFechaHoraActual());
        db.insert("TABLA_NOTAS",null,valores);

      return 1;
    }

    public int actualizarNota(int idNota,String nota) {
        abreBD();
       /* String sql="UPDATE TABLA_NOTAS SET NOTA ='" +nota+ "' WHERE ID_NOTA = "+idNota;
        db.execSQL(sql);
        */
        ContentValues valores = new ContentValues();
        valores.put("NOTA", nota);
        db.update("TABLA_NOTAS",valores,"ID_NOTA = ?", new String[] {idNota+""});
        return 1;
    }

    public int borrarNota(long rowid) {
        // COMPLETAR
       /*
        String sql="DELETE FROM TABLA_NOTAS WHERE ID_NOTA="+rowid;
        db.execSQL(sql);
        */

        String condicion[]= {String.valueOf(rowid)};
        db.delete("TABLA_NOTAS","ID_NOTA=?",condicion);
        return 1;
    }

    public String compartir(long rowid){

        String col[]={"NOTA"};
        String par[]={rowid+""};
        String nota="";

        Cursor f=db.query("TABLA_NOTAS",col,"ID_NOTA=?",par,null,null,null);

        if(f.getCount()>0){
            f.moveToNext();
            int i=f.getColumnIndex("NOTA");
            nota=f.getString(i);
        }

        return nota;
    }


}
