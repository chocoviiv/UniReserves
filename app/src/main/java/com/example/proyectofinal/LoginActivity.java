package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText etCorreoLogin, etContraseñalogin;
    Button bIniciarSesion, bRegistrarseLogin, bIr;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Inicializamos Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        etCorreoLogin = findViewById(R.id.et_correoLogin);
        etContraseñalogin = findViewById(R.id.et_contraseñaLogin);
        bIniciarSesion = findViewById(R.id.b_iniciarSesion);
        bRegistrarseLogin = findViewById(R.id.b_registrarseLogin);
        //bIr = findViewById(R.id.b_ir);

        bIniciarSesion.setOnClickListener(v -> {
            String correo = etCorreoLogin.getText().toString();
            String contraseña = etContraseñalogin.getText().toString();
            if (correo.isEmpty() || contraseña.isEmpty()) {
                Toast.makeText(this, "Ingrese correo y contraseña", Toast.LENGTH_SHORT).show();
            } else {
                IniciarSesion(correo, contraseña);
            }
        });

        bRegistrarseLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, RegistrarActivity.class));
        });

        bIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });

    }

    private void IniciarSesion(String correo, String contraseña) {
        mAuth.signInWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Inicio de sesión exitoso
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        String id = user.getUid(); // Obtén el UID del usuario
                        finish();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(LoginActivity.this, "Error al iniciar Sesión", Toast.LENGTH_SHORT).show();
        });
    }


    //si hay un usuario que a iniciado sesión, entonces ya es necesario que entre a Login
   /* @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();//Para ver si hay una sesión iniciada
        if (user != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }*/

}