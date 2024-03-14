package com.example.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import Modelo.Local;
import VistaModelo.VMLocal;

public class MainActivity extends AppCompatActivity {
    Button binicio, bReservas, bPerfil, bEventos, bConferencias, bDeportes, bGeneral;
    Spinner spLocales;
    public VMLocal vmLocal;
    RecyclerView rvLocales;

    ArrayAdapter<String> adapter;
    LocalAdapter localAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //toolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Reserva LocalesUNC");
        setSupportActionBar(toolbar);

        vmLocal = new VMLocal(this);


        binicio = findViewById(R.id.b_inicioMain);
        bReservas = findViewById(R.id.b_reservasMain);
        bPerfil = findViewById(R.id.b_perfilMain);
        bEventos = findViewById(R.id.b_eventos);
        bConferencias = findViewById(R.id.b_conferencias);
        bDeportes = findViewById(R.id.b_deportes);
        bGeneral = findViewById(R.id.b_general);
        rvLocales = findViewById(R.id.rv_locales);

        spLocales = findViewById(R.id.sp_locales);
        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, vmLocal.formatoCadenaLocales());
        spLocales.setAdapter(adapter);

        spLocales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String localSeleccionado = (String) parent.getItemAtPosition(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvLocales.setLayoutManager(linearLayoutManager);

        localAdapter = new LocalAdapter(this, vmLocal);

        bConferencias.setOnClickListener(v -> {
            adapter.clear();
            adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, vmLocal.LocalesConferencias());
            spLocales.setAdapter(adapter);
            localAdapter = new LocalAdapter(this, vmLocal.ListasE());
            rvLocales.setAdapter(localAdapter);
            EjecutarBotonReserva();
        });

        bDeportes.setOnClickListener(v -> {
            adapter.clear();
            adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, vmLocal.LocalesDeportes());
            spLocales.setAdapter(adapter);
            localAdapter = new LocalAdapter(this, vmLocal.ListasE());
            rvLocales.setAdapter(localAdapter);
            EjecutarBotonReserva();
        });


        bEventos.setOnClickListener(v -> {
            adapter.clear();
            adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, vmLocal.LocalesEventos());
            spLocales.setAdapter(adapter);
            localAdapter = new LocalAdapter(this, vmLocal.ListasE());
            rvLocales.setAdapter(localAdapter);
            EjecutarBotonReserva();
        });

        bGeneral.setOnClickListener(v -> {
            adapter.clear();
            adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, vmLocal.formatoCadenaLocales());
            spLocales.setAdapter(adapter);
            localAdapter = new LocalAdapter(this, vmLocal);
            rvLocales.setAdapter(localAdapter);
            EjecutarBotonReserva();

        });

        rvLocales.setAdapter(localAdapter);

        localAdapter.setOnLocalClickListener(new LocalAdapter.OnLocalClickListener() {
            @Override
            public void OnLocalClick(Local local, int llamada) {
                if (llamada == 1) {
                    Intent intent = new Intent(MainActivity.this, ReservarActivity.class);
                    startActivity(intent);
                }
            }
        });

        binicio.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainActivity.class)));


        bReservas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReservasActivity.class);
            startActivity(intent);
        });

        bPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PerfilActivity.class);
            startActivity(intent);
        });
    }

    public void EjecutarBotonReserva() {
        localAdapter.setOnLocalClickListener(new LocalAdapter.OnLocalClickListener() {
            @Override
            public void OnLocalClick(Local local, int llamada) {
                if (llamada == 1) {
                    Intent intent = new Intent(MainActivity.this, ReservarActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}