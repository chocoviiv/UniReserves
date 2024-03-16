package com.example.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Modelo.Reserva;
import VistaModelo.VMReserva;

public class ReservasActivity extends AppCompatActivity {
    Button binicio, bReservas, bPerfil;
    RecyclerView rvListadoReservas;
    FirebaseAuth auth;
    FirebaseUser user;
    VMReserva vmReserva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);

        //toolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Reservas Hechas");
        setSupportActionBar(toolbar);

        //para agregar la flecha de navegar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReservasActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        vmReserva = new VMReserva();
        vmReserva.listaReserva(this, user.getEmail());//Cargar la lista de reservas
        binicio = findViewById(R.id.b_inicioReserva);
        bReservas = findViewById(R.id.b_reservasReserva);
        bPerfil = findViewById(R.id.b_perfilReserva);
        rvListadoReservas = findViewById(R.id.rv_listadoReservas);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvListadoReservas.setLayoutManager(linearLayoutManager);

        ReservasAdapter reservasAdapter = new ReservasAdapter(ReservasActivity.this, vmReserva);
        rvListadoReservas.setAdapter(reservasAdapter);

        reservasAdapter.setOnReservaClickListener(new ReservasAdapter.OnReservaClickListener() {
            @Override
            public void onReservaClick(Reserva reserva, int llamada) {
                if (llamada == 1) {
                    Intent intent = new Intent(ReservasActivity.this, ModificarReservaActivity.class);
                    intent.putExtra("reserva", reserva);
                    startActivity(intent);
                }
                if (llamada == 2) {
                    EliminarReserva();
                    Intent intent = new Intent(ReservasActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });


        if (inicioSesion()) {
            bPerfil.setText("Perfil");
        } else {
            bPerfil.setText("Login");
        }


        binicio.setOnClickListener(v -> {
            Intent intent = new Intent(ReservasActivity.this, MainActivity.class);
            startActivity(intent);
        });

        bReservas.setOnClickListener(v -> {
            Intent intent = new Intent(ReservasActivity.this, ReservasActivity.class);
            startActivity(intent);
        });

        bPerfil.setOnClickListener(v -> {
            Intent intent;
            if (inicioSesion()) {
                intent = new Intent(ReservasActivity.this, PerfilActivity.class);
            } else {
                intent = new Intent(ReservasActivity.this, LoginActivity.class);
            }
            startActivity(intent);
        });

    }


    public boolean EliminarReserva() {
        int idReserva = 0;
        idReserva = vmReserva.ObtenerIdReserva(ReservasActivity.this, user.getEmail());
        if (idReserva > 0) {
            Toast.makeText(ReservasActivity.this, "Reserva Eliminada", Toast.LENGTH_SHORT).show();
            return vmReserva.ElimianarReserva(ReservasActivity.this, idReserva);
        }
        Toast.makeText(ReservasActivity.this, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
        return false;
    }

    public boolean inicioSesion() {
        if (user != null) {
            return true;
        }
        return false;
    }

}