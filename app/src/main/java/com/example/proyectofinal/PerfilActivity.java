package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import Modelo.Cliente;
import VistaModelo.VMCliente;

public class PerfilActivity extends AppCompatActivity {
    Button binicio, bReservas, bPerfil, bEditarPerfil;
    ImageView ivImagenUsuario;
    TextView tvNombreUsuario, tvCorreo, tvCelular, tvDireccion, tvTipo;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;
    VMCliente vmCliente;
    Cliente cliente = null;

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

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        vmCliente = new VMCliente();

        tvNombreUsuario = findViewById(R.id.tv_NomUsuario);
        tvCorreo = findViewById(R.id.tv_correo);
        tvCelular = findViewById(R.id.tv_celular);
        tvDireccion = findViewById(R.id.tv_direccion);
        tvTipo = findViewById(R.id.tv_tipo);
        ivImagenUsuario = findViewById(R.id.iv_imagenUsuario);

        binicio = findViewById(R.id.b_inicioPerfil);
        bReservas = findViewById(R.id.b_reservasPerfil);
        bPerfil = findViewById(R.id.b_perfilPerfil);
        bEditarPerfil = findViewById(R.id.bt_editarPerfil);

        if (user != null) {
            String id = user.getUid();
            DocumentReference documentReference = firebaseFirestore.collection("Usuario").document(id);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String nombres = documentSnapshot.getString("nombres");
                        String apellidos = documentSnapshot.getString("apellidos");
                        String celular = documentSnapshot.getString("celular");
                        String dni = documentSnapshot.getString("dni");
                        String direccion = documentSnapshot.getString("direccion");
                        String tipo = documentSnapshot.getString("tipo");
                        String contraseña = documentSnapshot.getString("contraseña");

                        if (imagenNull()) {
                            cliente = new Cliente(nombres, apellidos, celular, dni, direccion, user.getEmail(), contraseña);
                        } else {
                            // El ImageView tiene una imagen diferente asignada
                            if (cliente.getImagen() != null) {
                                ivImagenUsuario.setImageBitmap(decodificarByteBitMap(cliente.getImagen()));
                            }
                        }

                        tvNombreUsuario.setText(nombres + " " + apellidos);
                        tvCorreo.setText(user.getEmail());
                        tvCelular.setText(celular);
                        tvDireccion.setText(direccion);
                        tvTipo.setText(tipo);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } else {
            Toast.makeText(this, "Usuario null", Toast.LENGTH_SHORT).show();
        }


        if (inicioSesion()) {
            bPerfil.setText("Perfil");
        } else {
            bPerfil.setText("Login");
        }

        binicio.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
            startActivity(intent);
        });

        bReservas.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, ReservasActivity.class);
            startActivity(intent);
        });

        bPerfil.setOnClickListener(v -> {
            Intent intent;
            if (inicioSesion()) {
                intent = new Intent(PerfilActivity.this, PerfilActivity.class);
            } else {
                intent = new Intent(PerfilActivity.this, LoginActivity.class);
            }
            startActivity(intent);
        });

        bEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilActivity.this, EditarPerfilActivity.class);
                if (cliente != null) {
                    intent.putExtra("cliente", cliente);
                }
                startActivity(intent);
            }
        });

    }

    private Bitmap decodificarByteBitMap(byte[] imagen) {
        return BitmapFactory.decodeByteArray(imagen, 0, imagen.length);
    }

    public boolean inicioSesion() {
        if (user != null) {
            return true;
        }
        return false;
    }

    public boolean imagenNull() {
        for (Cliente dato : vmCliente.listarClientes(PerfilActivity.this)) {
            if (dato.getCorreo().equals(user.getEmail())) {
                if (dato.getImagen() == null) {
                    return true;
                }
                cliente = dato;
            }
        }
        return false;
    }
}