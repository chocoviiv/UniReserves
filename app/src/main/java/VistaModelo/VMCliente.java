package VistaModelo;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import Modelo.Cliente;
import Modelo.Local;

public class VMCliente {
    private ArrayList<Cliente> listaClientes;
    String nombreBD;
    int version;

    public VMCliente() {
        listaClientes = new ArrayList<>();
        this.nombreBD = "BDReservas";
        this.version = 1;
    }

    public boolean AgregarCliente(Activity oActivity, Cliente cliente) {
        boolean rpta = false;
        BDReservasOpenHelper bdReservasOpenHelper = new BDReservasOpenHelper(oActivity, nombreBD, null, version);
        SQLiteDatabase database = bdReservasOpenHelper.getWritableDatabase();
        if (database != null) {
            ContentValues registro = new ContentValues();
            registro.put("nombres", cliente.getNombres());
            registro.put("apellidos", cliente.getApellidos());
            registro.put("celular", cliente.getCelular());
            registro.put("dni", cliente.getDni());
            registro.put("direccion", cliente.getDireccion());
            registro.put("tipo", cliente.getTipo());
            registro.put("imagen", cliente.getImagen());
            registro.put("correo", cliente.getCorreo());
            registro.put("contraseña", cliente.getContraseña());
            long fila = database.insert("Cliente", null, registro);
            //en el segundo parámetro la base de datos asume que puedes insertar filas con valores nulos en cualquier
            // columna excepto las que tienen restricciones NOT NULL
            if (fila > 0) {
                rpta = true;
                listaClientes.add(cliente);
            }
        }
        return rpta;
    }


    public boolean ModificarCliente(Activity activity, Cliente cliente, int id) {
        boolean rpta = false;
        BDReservasOpenHelper bdReservasOpenHelper = new BDReservasOpenHelper(activity, nombreBD, null, version);
        SQLiteDatabase database = bdReservasOpenHelper.getWritableDatabase();
        if (database != null) {
            ContentValues registro = new ContentValues();
            registro.put("nombres", cliente.getNombres());
            registro.put("apellidos", cliente.getApellidos());
            registro.put("celular", cliente.getCelular());
            registro.put("dni", cliente.getDni());
            registro.put("direccion", cliente.getDireccion());
            registro.put("imagen", cliente.getImagen());

            int filas = (int) database.update("Cliente", registro, "IdCliente=" + id, null);
            database.close();
            if (filas > 0) {
                rpta = true;
            }
        }
        return rpta;
    }

    public boolean ModificarContraCliente(Activity activity, String correo, String contraseña, int id) {
        boolean rpta = false;
        BDReservasOpenHelper bdReservasOpenHelper = new BDReservasOpenHelper(activity, nombreBD, null, version);
        SQLiteDatabase database = bdReservasOpenHelper.getWritableDatabase();
        if (database != null) {
            ContentValues registro = new ContentValues();
            registro.put("correo", correo);
            registro.put("contraseña", contraseña);

            int filas = (int) database.update("Cliente", registro, "IdCliente=" + id, null);
            database.close();
            if (filas > 0) {
                rpta = true;
            }
        }
        return rpta;
    }


    public int ObtenerIdCliente(Activity oActivity, String correo) {
        int id = -1;
        BDReservasOpenHelper bdReservasOpenHelper = new BDReservasOpenHelper(oActivity, nombreBD, null, version);
        SQLiteDatabase database = bdReservasOpenHelper.getReadableDatabase();
        if (database != null) {
            Cursor registro = database.rawQuery("SELECT IdCliente FROM Cliente WHERE correo='" + correo + "'", null);
            if (registro.moveToFirst()) {
                id = registro.getInt(0);
            }
            database.close();
        }
        return id;
    }

    public Cliente ClienteCorreo(Activity oActivity, String correo) {
        Cliente cliente = null;
        BDReservasOpenHelper bdReservasOpenHelper = new BDReservasOpenHelper(oActivity, nombreBD, null, version);
        SQLiteDatabase database = bdReservasOpenHelper.getReadableDatabase();
        if (database != null) {
            Cursor registro = database.rawQuery("SELECT * FROM Cliente WHERE correo='" + correo + "'", null);
            if (registro.moveToFirst()) {
                String nombres = registro.getString(1);
                String apellidos = registro.getString(2);
                String celular = registro.getString(3);
                String dni = registro.getString(4);
                String direccion = registro.getString(5);
                String tipo = registro.getString(6);
                byte[] imagen = registro.getBlob(7);
                String correoE = registro.getString(8);
                String contraseña = registro.getString(9);
                cliente = new Cliente(nombres, apellidos, celular, dni, direccion, imagen, correoE, contraseña);
            }
            database.close();
        }
        return cliente;
    }

    public Cliente ClienteID(Activity oActivity, int id) {
        Cliente cliente = null;
        BDReservasOpenHelper bdReservasOpenHelper = new BDReservasOpenHelper(oActivity, nombreBD, null, version);
        SQLiteDatabase database = bdReservasOpenHelper.getReadableDatabase();
        if (database != null) {
            Cursor registro = database.rawQuery("SELECT * FROM Cliente WHERE IdCliente=" + id, null);
            if (registro.moveToFirst()) {
                String nombres = registro.getString(1);
                String apellidos = registro.getString(2);
                String celular = registro.getString(3);
                String dni = registro.getString(4);
                String direccion = registro.getString(5);
                String tipo = registro.getString(6);
                byte[] imagen = registro.getBlob(7);
                String correoE = registro.getString(8);
                String contraseña = registro.getString(9);
                cliente = new Cliente(nombres, apellidos, celular, dni, direccion, imagen, correoE, contraseña);
            }
            database.close();
        }
        return cliente;
    }

    public ArrayList<Cliente> listarClientes(Activity oActivity) {
        BDReservasOpenHelper bdReservasOpenHelper = new BDReservasOpenHelper(oActivity, nombreBD, null, version);
        SQLiteDatabase database = bdReservasOpenHelper.getReadableDatabase();
        Cursor oRegistros = database.rawQuery("select * from Cliente", null);
        Cliente cliente = null;
        if (oRegistros.moveToFirst()) {
            do {
                String nombres = oRegistros.getString(1);
                String apellidos = oRegistros.getString(2);
                String celular = oRegistros.getString(3);
                String dni = oRegistros.getString(4);
                String direccion = oRegistros.getString(5);
                String tipo = oRegistros.getString(6);
                byte[] imagen = oRegistros.getBlob(7);
                String correo = oRegistros.getString(8);
                String contraseña = oRegistros.getString(9);
                cliente = new Cliente(nombres, apellidos, celular, dni, direccion, imagen, correo, contraseña);
                listaClientes.add(cliente);
            } while (oRegistros.moveToNext());
            database.close();
        }
        return listaClientes;
    }

    public Cliente ObtenerCliente(int pos) {
        return listaClientes.get(pos);
    }

    public int ContarClientes() {
        return listaClientes.size();
    }

}