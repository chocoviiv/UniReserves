package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import Modelo.Cliente;
import VistaModelo.VMCliente;

public class RegistrarActivity extends AppCompatActivity {
    EditText etNombres, etApellidos, etCelular, etDni, etDireccion, etCorreo, etContraseña;
    Button bRegistrar;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;

    VMCliente vmCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        etNombres = findViewById(R.id.et_nombres);
        etApellidos = findViewById(R.id.et_apellidos);
        etCelular = findViewById(R.id.et_celular);
        etDni = findViewById(R.id.et_dni);
        etDireccion = findViewById(R.id.et_direccion);
        etCorreo = findViewById(R.id.et_correo);
        etContraseña = findViewById(R.id.et_contraseña);

        bRegistrar = findViewById(R.id.b_registrar);
        //bIrInicio = findViewById(R.id.b_irInicio);

        bRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombres = etNombres.getText().toString();
                String apellidos = etApellidos.getText().toString();
                String celular = etCelular.getText().toString();
                String dni = etDni.getText().toString();
                String direccion = etDireccion.getText().toString();
                String correo = etCorreo.getText().toString().trim();
                String contraseña = etContraseña.getText().toString().trim();

                // Verificar si algún campo está vacío
                if (nombres.isEmpty() || apellidos.isEmpty() || celular.isEmpty() || dni.isEmpty() || direccion.isEmpty() || correo.isEmpty() || contraseña.isEmpty()) {
                   Toast.makeText(RegistrarActivity.this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    RegistrarUsuario();
                    Intent intent = new Intent(RegistrarActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void RegistrarUsuario() {
        String nombres = etNombres.getText().toString();
        String apellidos = etApellidos.getText().toString();
        String celular = etCelular.getText().toString();
        String dni = etDni.getText().toString();
        String direccion = etDireccion.getText().toString();
        String correo = etCorreo.getText().toString().trim();
        String tipo = esUnc(correo);
        String contraseña = etContraseña.getText().toString().trim();
        if (nombres.isEmpty() || apellidos.isEmpty() || celular.isEmpty() || dni.isEmpty() || direccion.isEmpty() || correo.isEmpty() || contraseña.isEmpty()) {
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String id = mAuth.getCurrentUser().getUid();
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", id);
                            map.put("nombres", nombres);
                            map.put("apellidos", apellidos);
                            map.put("celular", celular);
                            map.put("dni", dni);
                            map.put("direccion", direccion);
                            map.put("tipo", tipo);
                            map.put("correo", correo);
                            map.put("contraseña", contraseña);

                            firebaseFirestore.collection("Usuario").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    finish();
                                    Cliente cliente = new Cliente(nombres, apellidos, celular, dni, direccion, correo, contraseña);
                                    cliente.setId(id);
                                    cliente.setTipo(tipo);
                                    vmCliente = new VMCliente();
                                    if (vmCliente.AgregarCliente(RegistrarActivity.this, cliente)) {
                                        // Pasar datos del cliente a PerfilActivity
                                        Intent intent = new Intent(RegistrarActivity.this, PerfilActivity.class);
                                        intent.putExtra("cliente", cliente);
                                        startActivity(intent);
                                        Toast.makeText(RegistrarActivity.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RegistrarActivity.this, "Error al registrar en SQLite", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegistrarActivity.this, "Error al registrar en Firestore", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(RegistrarActivity.this, "Error al obtener el usuario actual", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegistrarActivity.this, "Error al registrar en Firebase Authentication", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public String esUnc(String correo) {
        if (correo.matches(".*@unc\\.edu\\.pe")) {
            return "Miembro de la UNC";
        }
        return "Externo a UNC";
    }
}