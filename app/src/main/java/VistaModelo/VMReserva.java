package VistaModelo;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import Modelo.Cliente;
import Modelo.Local;
import Modelo.Reserva;

public class VMReserva {
    private ArrayList<Reserva> listaReservas;
    String nombreBD;
    int version;
    VMLocal vmLocal;
    VMCliente vmCliente;

    public VMReserva() {
        listaReservas = new ArrayList<>();
        this.nombreBD = "BDReservas";
        this.version = 1;
    }

    public boolean AgregarReserva(Activity activity, Reserva reserva, int idLocal, int idCliente) {
        boolean rpta = false;
        BDReservasOpenHelper bdReservasOpenHelper = new BDReservasOpenHelper(activity, nombreBD, null, version);
        SQLiteDatabase database = bdReservasOpenHelper.getWritableDatabase();

        if (database != null) {
            ContentValues registros = new ContentValues();
            registros.put("IdLocal", idLocal);
            registros.put("IdCliente", idCliente);
            registros.put("FechaInicio", reserva.getFechaInicio().toString());
            registros.put("FechaFinal", reserva.getFechaFinal().toString());
            registros.put("Descripcion", reserva.getDescripcionActi());
            registros.put("costo", reserva.getCosto());
            listaReservas.add(reserva);
            long fila = database.insert("Reserva", null, registros);
            if (fila > 0) {
                rpta = true;
            }
            database.close();
        }
        return rpta;
    }

    public ArrayList<Reserva> listaReserva(Activity activity, String correo) {
        BDReservasOpenHelper bdReservasOpenHelper = new BDReservasOpenHelper(activity, nombreBD, null, version);
        SQLiteDatabase database = bdReservasOpenHelper.getReadableDatabase();
        Cursor oRegistros = database.rawQuery("SELECT * FROM Reserva " +
                "INNER JOIN Cliente ON Reserva.IdCliente = Cliente.IdCliente " +
                "WHERE Cliente.correo = ?", new String[]{correo});
        vmCliente = new VMCliente();
        vmLocal = new VMLocal(activity);
        Cliente cliente = null;
        Local local = null;
        Reserva reserva = null;
        listaReservas.clear();

        if (oRegistros.moveToFirst()) {
            do {
                int idLocal = oRegistros.getInt(1);
                int idCliente = oRegistros.getInt(2);
                String fechaIni = oRegistros.getString(3);
                String fechaFin = oRegistros.getString(4);
                String descripcion = oRegistros.getString(5);
                double costo = oRegistros.getDouble(6);
                cliente = vmCliente.ClienteID(activity, idCliente);
                local = vmLocal.LocalID(activity, idLocal);
                reserva = new Reserva(cliente, local, descripcion, convertirStrinToDate(fechaIni), convertirStrinToDate(fechaFin), costo);
                listaReservas.add(reserva);
            } while (oRegistros.moveToNext());
        }
        database.close();
        return listaReservas;
    }


    public ArrayList<Date> obtenerFechasReservadas(Activity activity, int idLocal) {
        ArrayList<Date> fechasReservadas = new ArrayList<>();
        BDReservasOpenHelper bdReservasOpenHelper = new BDReservasOpenHelper(activity, nombreBD, null, version);
        SQLiteDatabase database = bdReservasOpenHelper.getReadableDatabase();
        Cursor oRegistros = database.rawQuery("SELECT FechaInicio, FechaFinal FROM Reserva WHERE IdLocal =" + idLocal, null);
        if (oRegistros.moveToFirst()) {
            do {
                String fechaInicioStr = oRegistros.getString(0);
                String fechaFinStr = oRegistros.getString(1);
                // Convertir las cadenas de fecha a objetos Date y agregarlas a la lista
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault());
                try {
                    Date fechaInicio = sdf.parse(fechaInicioStr);
                    Date fechaFin = sdf.parse(fechaFinStr);
                    fechasReservadas.add(fechaInicio);
                    fechasReservadas.add(fechaFin);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (oRegistros.moveToNext());
        }
        database.close();
        return fechasReservadas;
    }

    public Date convertirStrinToDate(String cadena) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault());
        Date fecha = null;
        try {
            fecha = formatter.parse(cadena);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return fecha;
    }

    public Reserva ObtenerReserva(int pos) {
        return listaReservas.get(pos);
    }

    public int sizeReservas() {
        return listaReservas.size();
    }

    public void eliminarReserva(Reserva reserva) {
        if(listaReservas!= null && listaReservas.contains(reserva)){
            listaReservas.remove(reserva);
        }
    }
}
