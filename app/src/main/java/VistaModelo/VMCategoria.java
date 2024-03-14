package VistaModelo;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import Modelo.Categoria;

public class VMCategoria {

    ArrayList<Categoria> listaCategoria;
    String nombreBD;
    Activity oActivity;

    int version;

    public VMCategoria(Activity oActivity) {
        listaCategoria = new ArrayList<>();
        this.nombreBD = "BDReservas";
        this.version = 1;
        this.oActivity = oActivity;
        InsertarRegistroPrueba();
    }

    private void InsertarRegistroPrueba() {
        if (!CargarLista()) {
            Categoria categoria1 = new Categoria("01", "Conferencias");
            Categoria categoria2 = new Categoria("02", "Deportes");
            Categoria categoria3 = new Categoria("03", "Eventos");

            AgregarCategoria(categoria1);
            AgregarCategoria(categoria2);
            AgregarCategoria(categoria3);
        }
    }


    private boolean AgregarCategoria(Categoria categoria) {
        boolean rpta = false;
        BDReservasOpenHelper bdPlatosOpenHelper = new BDReservasOpenHelper(oActivity, nombreBD, null, version);
        SQLiteDatabase database = bdPlatosOpenHelper.getWritableDatabase();
        if (bdPlatosOpenHelper != null) {
            ContentValues registro = new ContentValues();
            registro.put("IdCategoria", categoria.getIdCategoria());
            registro.put("nombreCategoria", categoria.getNombreCategoria());//Abro la base de datos como escritura
            listaCategoria.add(categoria);
            //Insertamos la fila en la tabla.
            long fila = database.insert("Categoria", null, registro);
            if (fila > 0) {
                rpta = true;
            }
            database.close();
        }
        return rpta;
    }


    public boolean CargarLista() {
        boolean rpta = false;
        listaCategoria.clear();//Limpiamos la lista para que no se repitan
        BDReservasOpenHelper openHelper = new BDReservasOpenHelper(oActivity, nombreBD, null, version);
        SQLiteDatabase database = openHelper.getReadableDatabase();//Abro la base de datos como lectura
        Cursor regitros = database.rawQuery("select * from Categoria", null);
        if (regitros.moveToFirst()) {
            //Si moveToFirst() tiene registros entrará al if
            rpta = true;
            do {
                String idPCategoria = regitros.getString(0);
                String nombreProvincia = regitros.getString(1);
                Categoria categoria = new Categoria(idPCategoria, nombreProvincia);
                listaCategoria.add(categoria);
            } while (regitros.moveToNext());//mientras pueda ir al siguiente registro
            database.close();
        }
        return rpta;
    }

    public Categoria getCategoria(int indice) {
        return listaCategoria.get(indice);
    }

    public int getIndiceId(String categoria) {
        int indice = -1;
        for (Categoria oProvincia : listaCategoria) {
            if (categoria.equals(oProvincia.getIdCategoria())) {
                indice = listaCategoria.indexOf(oProvincia);
            }
        }
        return indice;
    }

    public ArrayList<Categoria> getListaCategoria() {
        return listaCategoria;
    }

}
