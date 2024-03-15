package VistaModelo;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.proyectofinal.R;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import Modelo.Local;

public class VMLocal {
    private ArrayList<Local> listaLocales;
    private ArrayList<Local> localesEspecificos;
    String nombreBD;
    int version;
    Activity oActivity;

    VMCategoria vmCategoria;

    //Agregue para la busqueda
    public ArrayList<Local> obtenerLocales(String query) {
        ArrayList<Local> localesFiltrados = new ArrayList<>();
        for (Local local : listaLocales) {
            if (local.getNombre().toLowerCase().contains(query.toLowerCase())) {
                localesFiltrados.add(local);
            }
        }
        return localesFiltrados;
    }
    //Agregué para la busqueda
    public ArrayList<Local> filtrarLocalesPorNombre(String query) {
        ArrayList<Local> localesFiltrados = new ArrayList<>();
        for (Local local : listaLocales) {
            if (local.getNombre().toLowerCase().contains(query.toLowerCase())) {
                localesFiltrados.add(local);
            }
        }
        return localesFiltrados;
    }
    public VMLocal(Activity oActivity) {
        listaLocales = new ArrayList<>();
        localesEspecificos = new ArrayList<>();
        this.oActivity = oActivity;
        this.nombreBD = "BDReservas";
        this.version = 1;
        InsertarLocales();
    }

    private void InsertarLocales() {
        if (!CargarLista()) {
            Local local1 = new Local("Aula Magna", vmCategoria.getCategoria(0), DecodificarIntToBytes(R.drawable.aulamg), 200.20, "Ubicada antes del edificio 2A, ingreso por la puerta principal", "Libre");
            Local local2 = new Local("Cancha de Fútbol (Estadio de la UNC)", vmCategoria.getCategoria(2), DecodificarIntToBytes(R.drawable.estadio), 50.10, "Ubicado frente a la Escuela de Geología", "Libre");
            Local local3 = new Local("Centro de Convenciones César Alipio Paredes Canto", vmCategoria.getCategoria(0), DecodificarIntToBytes(R.drawable.convenciones), 150.20, "Ubicado en Jr. Dos de Mayo # 362", "Libre");
            Local local4 = new Local("Coliseo UNC", vmCategoria.getCategoria(1), DecodificarIntToBytes(R.drawable.coliseo), 130.20, "Ubicado en la parte inferior de la entrada principal", "Libre");
            Local local5 = new Local("Ex Banco Agrario", vmCategoria.getCategoria(1), DecodificarIntToBytes(R.drawable.bancoagr), 190.20, "Ubicado en Jr. Amalia Puga # 516", "Libre");
            Local local6 = new Local("Plataforma Deportiva Nº 1", vmCategoria.getCategoria(2), DecodificarIntToBytes(R.drawable.plataforma1), 190.20, "Ubicada a espaldas de la Facultad de Medicina", "Libre");
            Local local7 = new Local("Plataforma Deportiva Nº 2", vmCategoria.getCategoria(2), DecodificarIntToBytes(R.drawable.plataforma2), 190.20, "Ubicada a espaldas del Coliseo de la UNC", "Libre");
            Local local8 = new Local("Plataforma Deportiva Nº 3", vmCategoria.getCategoria(2), DecodificarIntToBytes(R.drawable.plataforma3), 190.20, "Ubicada frente al paradero universitario dentro de la UNC", "Libre");

            AgregarLocal(local1);
            AgregarLocal(local2);
            AgregarLocal(local3);
            AgregarLocal(local4);
            AgregarLocal(local5);
            AgregarLocal(local6);
            AgregarLocal(local7);
            AgregarLocal(local8);
        }
    }

    private byte[] DecodificarIntToBytes(int entero) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(entero);
        return buffer.array();
    }

    public boolean AgregarLocal(Local local) {
        boolean rpta = false;
        BDReservasOpenHelper bdReservasOpenHelper = new BDReservasOpenHelper(oActivity, nombreBD, null, version);
        SQLiteDatabase database = bdReservasOpenHelper.getWritableDatabase();
        if (bdReservasOpenHelper != null) {
            ContentValues registro = new ContentValues();
            registro.put("nombre", local.getNombre());
            registro.put("IdCategoria", local.getCategoria().getIdCategoria());
            registro.put("imagen", local.getImagen());
            registro.put("costo", local.getPrecio());
            registro.put("ubicación", local.getUbicacion());
            registro.put("disponibilidad", local.getDisponibildiad());
            listaLocales.add(local);
            long fila = database.insert("Local", null, registro);
            if (fila > 0) {
                rpta = true;
            }
            database.close();
        }
        return rpta;
    }

    public boolean CargarLista() {
        vmCategoria = new VMCategoria(oActivity);
        boolean rpta = false;
        listaLocales.clear();//Limpiamos la lista para que no se repitan
        BDReservasOpenHelper openHelper = new BDReservasOpenHelper(oActivity, nombreBD, null, version);
        SQLiteDatabase database = openHelper.getReadableDatabase();
        Cursor registros = database.rawQuery("select * from Local", null);
        if (registros.moveToFirst()) {
            rpta = true;
            do {
                String nombre = registros.getString(1);
                String idCateg = registros.getString(2);
                int indiceCateg = vmCategoria.getIndiceId(idCateg);
                byte[] imagen = registros.getBlob(3);
                Double precio = registros.getDouble(4);
                String ubicacion = registros.getString(5);
                String disponibilidad = registros.getString(6);
                Local local = new Local(nombre, vmCategoria.getCategoria(indiceCateg), imagen, precio, ubicacion, disponibilidad);
                listaLocales.add(local);
            } while (registros.moveToNext());//mientras pueda ir al siguiente registro
            database.close();
        }
        return rpta;
    }
    public int ObtenerIdLocal(Activity oActivity, String nombre) {
        int id = -1;
        BDReservasOpenHelper bdReservasOpenHelper = new BDReservasOpenHelper(oActivity, nombreBD, null, version);
        SQLiteDatabase database = bdReservasOpenHelper.getReadableDatabase();
        if (database != null) {
            Cursor registro = database.rawQuery("SELECT IdLocal FROM Local WHERE nombre='" + nombre + "'", null);
            if (registro.moveToFirst()) {
                id = registro.getInt(0);
            }
            database.close();
        }
        return id;
    }
    public Local LocalID(Activity oActivity, int id) {
        vmCategoria = new VMCategoria(oActivity);
        Local local = null;
        BDReservasOpenHelper bdReservasOpenHelper = new BDReservasOpenHelper(oActivity, nombreBD, null, version);
        SQLiteDatabase database = bdReservasOpenHelper.getReadableDatabase();
        if (database != null) {
            Cursor registros = database.rawQuery("SELECT * FROM Local WHERE IdLocal=" + id, null);
            if (registros.moveToFirst()) {
                String nombre = registros.getString(1);
                String idCateg = registros.getString(2);
                int indiceCateg = vmCategoria.getIndiceId(idCateg);
                byte[] imagen = registros.getBlob(3);
                Double precio = registros.getDouble(4);
                String ubicacion = registros.getString(5);
                String disponibilidad = registros.getString(6);
                local = new Local(nombre, vmCategoria.getCategoria(indiceCateg), imagen, precio, ubicacion, disponibilidad);
            }
            database.close();
        }
        return local;
    }

    public Local ObtenerLocal(int pos) {
        return listaLocales.get(pos);
    }
    public int sizeLocal() {
        return listaLocales.size();
    }

    public ArrayList<String> formatoCadenaLocales() {
        ArrayList<String> lista = new ArrayList<>();
        for (Local local : listaLocales) {
            lista.add(local.getNombre());
        }
        return lista;
    }

    public ArrayList<String> LocalesConferencias() {
        localesEspecificos.clear();
        ArrayList<String> lista = new ArrayList<>();


        for (int i = 0; i < listaLocales.size(); i++) {
            if (listaLocales.get(i).getCategoria().getNombreCategoria().equalsIgnoreCase("Conferencias")) {
                lista.add(listaLocales.get(i).getNombre());
                localesEspecificos.add(listaLocales.get(i));
            }
        }
        return lista;
    }

    public ArrayList<String> LocalesDeportes() {
        localesEspecificos.clear();
        ArrayList<String> lista = new ArrayList<>();

        for (int i = 0; i < listaLocales.size(); i++) {
            if (listaLocales.get(i).getCategoria().getNombreCategoria().equalsIgnoreCase("Deportes")) {
                lista.add(listaLocales.get(i).getNombre());
                localesEspecificos.add(listaLocales.get(i));
            }
        }
        return lista;
    }

    public ArrayList<String> LocalesEventos() {
        localesEspecificos.clear();
        ArrayList<String> lista = new ArrayList<>();

        for (int i = 0; i < listaLocales.size(); i++) {
            if (listaLocales.get(i).getCategoria().getNombreCategoria().equalsIgnoreCase("Eventos")) {
                lista.add(listaLocales.get(i).getNombre());
                localesEspecificos.add(listaLocales.get(i));
            }
        }
        return lista;
    }

    public ArrayList<Local> ListasE() {
        return this.localesEspecificos;
    }

}
