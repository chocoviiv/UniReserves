package com.example.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PerfilActivity extends AppCompatActivity {
    Button binicio, bReservas, bPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        //toolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Perfil Usuario");
        setSupportActionBar(toolbar);

        //para agregar la flecha de navegar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        binicio = findViewById(R.id.b_inicioPerfil);
        bReservas = findViewById(R.id.b_reservasPerfil);
        bPerfil = findViewById(R.id.b_perfilPerfil);


        binicio.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
            startActivity(intent);
        });

        bReservas.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, ReservasActivity.class);
            startActivity(intent);
        });

        bPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, PerfilActivity.class);
            startActivity(intent);
        });


    }
}