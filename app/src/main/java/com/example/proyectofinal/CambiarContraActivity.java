package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import Modelo.Cliente;
import VistaModelo.VMCliente;

public class CambiarContraActivity extends AppCompatActivity {

    EditText etCorreoCambiar, etContraActual, etContraNueva;
    Button bSaveEmail, bRegresar;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;
    VMCliente vmCliente;
    int idCliente = 0;
    Cliente cliente = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contra);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        vmCliente = new VMCliente();

        etContraActual = findViewById(R.id.et_contraActual);
        etContraNueva = findViewById(R.id.et_contraNueva);
        etCorreoCambiar = findViewById(R.id.et_correoCambiar);
        etCorreoCambiar.setEnabled(false);
        etCorreoCambiar.setTextColor(getResources().getColor(R.color.black));
        bSaveEmail = findViewById(R.id.b_saveEmail);
        bRegresar = findViewById(R.id.b_regresar);

        Bundle datoCliente = getIntent().getExtras();
        cliente = (Cliente) datoCliente.getSerializable("cliente");
        if (user != null) {
            etCorreoCambiar.setText(user.getEmail());
        }

        bSaveEmail.setOnClickListener(v -> {
            if (etCorreoCambiar.getText().toString().isEmpty() || etContraActual.getText().toString().isEmpty() || etContraNueva.getText().toString().isEmpty()) {
                Toast.makeText(this, "Ingrese los datos correspondientes", Toast.LENGTH_SHORT).show();
            } else {
                cambiarDatos(etCorreoCambiar.getText().toString().trim(), etContraActual.getText().toString(), etContraNueva.getText().toString());
                startActivity(new Intent(CambiarContraActivity.this, MainActivity.class));
            }
        });

        bRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CambiarContraActivity.this, EditarPerfilActivity.class);
                intent.putExtra("cliente", cliente);
                startActivity(intent);
            }
        });
    }

    private void cambiarDatos(String correo, String contraActual, String contraNueva) {
        user = auth.getCurrentUser();
        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), contraActual);
            user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Reautenticación exitosa, ahora actualizamos el correo electrónico y la contraseña
                    user.updateEmail(correo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Actualización de correo electrónico exitosa
                                    user.updatePassword(contraNueva)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Actualización de contraseña exitosa
                                                    // Ahora actualizamos los datos en Firestore
                                                    actualizarEnBaseDatos(correo, contraActual, contraNueva);
                                                    Toast.makeText(CambiarContraActivity.this, "Cambios guardados exitosamente", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Manejar error al actualizar contraseña
                                                    Toast.makeText(CambiarContraActivity.this, "Error en contraseña", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Manejar error al actualizar correo electrónico
                                    Toast.makeText(CambiarContraActivity.this, "Error en correo", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Manejar error de reautenticación
                    Toast.makeText(CambiarContraActivity.this, "Error 1", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void actualizarEnBaseDatos(String correo, String contraActual, String contraNueva) {
        // Actualizar los datos en Firestore aquí
        idCliente = vmCliente.ObtenerIdCliente(CambiarContraActivity.this, user.getEmail());
        if (idCliente > 0) {
            DocumentReference documentReference = firebaseFirestore.collection("Usuario").document(user.getUid());
            Map<String, Object> updates = new HashMap<>();
            updates.put("correo", correo);
            updates.put("contraseña", contraNueva);
            documentReference.update(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Actualización en Firestore exitosa
                            // Puedes manejar cualquier resultado aquí, como mostrar un mensaje de éxito
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Manejar error al actualizar en Firestore
                        }
                    });
        }
        vmCliente.ModificarContraCliente(CambiarContraActivity.this, correo, contraNueva, idCliente);
    }
}