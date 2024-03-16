package com.example.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import Modelo.Local;
import VistaModelo.VMLocal;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    SearchView svSearch;
    Button binicio, bReservas, bPerfil, bEventos, bConferencias, bDeportes, bGeneral;
    Spinner spLocales;
    public VMLocal vmLocal;
    RecyclerView rvLocales;

    ArrayAdapter<String> adapter;
    LocalAdapter localAdapter;
    FirebaseAuth auth;
    FirebaseUser user;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, settings, share, about, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //toolBar
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.setTitle("Reserva LocalesUNC");
        //setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        about = findViewById(R.id.about);
        logout = findViewById(R.id.logout);
        settings = findViewById(R.id.settings);
        share = findViewById(R.id.share);
        svSearch = findViewById(R.id.svSearch);
        svSearch.setOnQueryTextListener(this);
        vmLocal = new VMLocal(this);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, SettingsActivity.class);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, ShareActivity.class);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, AboutActivity.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
            }
        });
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        vmLocal = new VMLocal(this);


        binicio = findViewById(R.id.b_inicioMain);
        bReservas = findViewById(R.id.b_reservasMain);
        bPerfil = findViewById(R.id.b_perfilMain);
        bEventos = findViewById(R.id.b_eventos);
        bConferencias = findViewById(R.id.b_conferencias);
        bDeportes = findViewById(R.id.b_deportes);
        bGeneral = findViewById(R.id.b_general);
        rvLocales = findViewById(R.id.rv_locales);
        svSearch = findViewById(R.id.svSearch);
        localAdapter = new LocalAdapter(this, vmLocal);
        rvLocales.setAdapter(localAdapter);
        if (inicioSesion()) {
            bPerfil.setText("Perfil");
        } else {
            bPerfil.setText("Login");
        }
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
            auth.signOut();
            finish();
            startActivity(new Intent(MainActivity.this, MainActivity.class));
           /* adapter.clear();
            adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, vmLocal.formatoCadenaLocales());
            spLocales.setAdapter(adapter);
            localAdapter = new LocalAdapter(this, vmLocal);
            rvLocales.setAdapter(localAdapter);
            EjecutarBotonReserva();*/

        });

        rvLocales.setAdapter(localAdapter);

        localAdapter.setOnLocalClickListener(new LocalAdapter.OnLocalClickListener() {
            @Override
            public void OnLocalClick(Local local, int llamada) {
                if (llamada == 1) {
                    Intent intent = new Intent(MainActivity.this, ReservarActivity.class);
                    intent.putExtra("local", local);
                    startActivity(intent);
                }
            }
        });

        binicio.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainActivity.class)));


        bReservas.setOnClickListener(v -> {
            Intent intent;
            if (inicioSesion()) {
                intent = new Intent(MainActivity.this, ReservasActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Para ver sus reservar, inicie sesión.", Toast.LENGTH_SHORT).show();
            }
        });

        bPerfil.setOnClickListener(v -> {
            Intent intent;
            if (inicioSesion()) {
                intent = new Intent(MainActivity.this, PerfilActivity.class);
            } else {
                intent = new Intent(MainActivity.this, LoginActivity.class);
            }
            startActivity(intent);
        });
    }

    public boolean inicioSesion() {
        if (user != null) {
            return true;
        }
        return false;
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    public boolean onQueryTextChange(String newText) {
        ArrayList<Local> localesFiltrados = vmLocal.obtenerLocales(newText);
        if (localesFiltrados != null) {
            localAdapter.setLocales(localesFiltrados);
            localAdapter.notifyDataSetChanged();
        }
        return true;
    }

}