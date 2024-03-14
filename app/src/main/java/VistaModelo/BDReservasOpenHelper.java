package VistaModelo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BDReservasOpenHelper extends SQLiteOpenHelper {
    String tabla_cliente = "CREATE TABLE Cliente ("
            + "IdCliente INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "nombres VARCHAR(50) NOT NULL," +
            "apellidos VARCHAR(50) NOT NULL," +
            "celular VARCHAR(9) NOT NULL," +
            "dni VARCHAR(8) NOT NULL UNIQUE," +
            "direccion VARCHAR(50) NOT NULL NOT NULL," +
            "tipo VARCHAR(50) NOT NULL NOT NULL," +
            "correo VARCHAR(50) NOT NULL NOT NULL UNIQUE," +
            "contraseña VARCHAR(50) NOT NULL NOT NULL)";

    String tabla_local = "CREATE TABLE Local ("
            + "IdLocal INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "nombre VARCHAR(50) NOT NULL," +
            "IdCategoria VARCHAR(2) NOT NULL," +
            "imagen BLOB NOT NULL," +
            "costo FLOAT NOT NULL," +
            "ubicación VARCHAR(8) NOT NULL UNIQUE," +
            "disponibilidad VARCHAR(50) NOT NULL NOT NULL)";

    String tabla_categoria = "CREATE TABLE Categoria(" +
            "IdCategoria VARCHAR(2) NOT NULL PRIMARY KEY," +
            "nombreCategoria VARCHAR(20) NOT NULL UNIQUE)";

    public BDReservasOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tabla_cliente);//Creamos la tabla
        db.execSQL(tabla_local);//
        db.execSQL(tabla_categoria);//
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Cliente");
        db.execSQL("DROP TABLE IF EXISTS Local");
        db.execSQL("DROP TABLE IF EXISTS Categoria");
        onCreate(db);
    }
}