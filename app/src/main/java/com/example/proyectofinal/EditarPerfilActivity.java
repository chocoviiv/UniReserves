package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import Modelo.Cliente;
import VistaModelo.VMCliente;

public class EditarPerfilActivity extends AppCompatActivity {

    ImageView ivImagenUserEdit;
    EditText etNombresEdit, etApellidosEdit, etCelularEdit, etDniEdit, etDireccionEdit;
    Button bGuardarCambios, bEditCorreoContra;
    FirebaseFirestore firebaseFirestore;
    String idUsuario;
    byte[] imagen;
    FirebaseAuth auth;
    FirebaseUser user;
    Cliente cliente = null;
    VMCliente vmCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        //toolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Editar perfil");
        setSupportActionBar(toolbar);

        //para agregar la flecha de navegar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //getOnBackPressedDispatcher().onBackPressed();
                Intent intent = new Intent(EditarPerfilActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });
        vmCliente = new VMCliente();
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        idUsuario = user.getUid();

        etNombresEdit = findViewById(R.id.et_nombresEditar);
        etApellidosEdit = findViewById(R.id.et_apellidosEditar);
        etCelularEdit = findViewById(R.id.et_celularEditar);
        etDniEdit = findViewById(R.id.et_dniEditar);
        ivImagenUserEdit = findViewById(R.id.iv_imagenUserEdit);
        etDireccionEdit = findViewById(R.id.et_direccionEditar);
        bGuardarCambios = findViewById(R.id.b_guardarCambios);
        bEditCorreoContra = findViewById(R.id.b_editCorreoContra);

        Bundle datoCliente = getIntent().getExtras();
        if (datoCliente != null) {
            cliente = (Cliente) datoCliente.getSerializable("cliente");
            etNombresEdit.setText(cliente.getNombres());
            etApellidosEdit.setText(cliente.getApellidos());
            etCelularEdit.setText(cliente.getCelular());
            etDniEdit.setText(cliente.getDni());
            etDireccionEdit.setText(cliente.getDireccion());
            if (cliente.getImagen() != null) {
                ivImagenUserEdit.setImageBitmap(decodificarByteBitMap(cliente.getImagen()));
            }
        }

        bEditCorreoContra.setOnClickListener(v -> {
            Intent intent = new Intent(EditarPerfilActivity.this, CambiarContraActivity.class);
            intent.putExtra("cliente", cliente);
            startActivity(intent);
        });

        bGuardarCambios.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarCliente(etNombresEdit.getText().toString(), etApellidosEdit.getText().toString(), etCelularEdit.getText().toString(), etDniEdit.getText().toString(), etDireccionEdit.getText().toString());
                finish();
            }
        });

        ivImagenUserEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImagen();
            }
        });
    }

    private Bitmap decodificarByteBitMap(byte[] imagen) {
        if (imagen != null) {
            return BitmapFactory.decodeByteArray(imagen, 0, imagen.length);
        }
        return null;
    }

    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// PARA PERMISOS DE FOTS de google Y accedemos a más carpetas
        //Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//SOlo accedemos a Pictures
        intent.setType("image/*");
        startActivityIfNeeded(Intent.createChooser(intent, "seleccione la aplicación"), 11);//El número puede ser cualquiera pero único, en este caso 11
    }

    //Para guardar la imagen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 11) {
            if (data != null && data.getData() != null) { // Verificar si se seleccionó una imagen
                ivImagenUserEdit.setImageURI(data.getData());
                ivImagenUserEdit.buildDrawingCache();
                Bitmap imagenBitMap = ivImagenUserEdit.getDrawingCache();
                ByteArrayOutputStream flujoSalida = new ByteArrayOutputStream();
                imagenBitMap.compress(Bitmap.CompressFormat.PNG, 0, flujoSalida);
                imagen = flujoSalida.toByteArray();
            } else {
                Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void actualizarCliente(String nombres, String apellidos, String celular, String dni, String direccion) {
        DocumentReference documentReference = firebaseFirestore.collection("Usuario").document(idUsuario);
        Map<String, Object> updates = new HashMap<>();
        updates.put("nombres", nombres);
        updates.put("apellidos", apellidos);
        updates.put("celular", celular);
        updates.put("dni", dni);
        updates.put("direccion", direccion);

        //Actualizar el cliente que le hemos pasado
        cliente.setNombres(nombres);
        cliente.setApellidos(apellidos);
        cliente.setCelular(celular);
        cliente.setDni(dni);
        cliente.setDireccion(direccion);
        if (imagen != null) {
            cliente.setImagen(imagen);
        }

        documentReference.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                int idCliente = 0;
                if (user != null) {
                    idCliente = vmCliente.ObtenerIdCliente(EditarPerfilActivity.this, user.getEmail());
                }
                if (idCliente > 0) {
                    if (vmCliente.ModificarCliente(EditarPerfilActivity.this, cliente, idCliente)) {
                        Toast.makeText(EditarPerfilActivity.this, "Campos actualizados correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditarPerfilActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditarPerfilActivity.this, "Id Incorrecto", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditarPerfilActivity.this, "Error al actualizar campos.", Toast.LENGTH_SHORT).show();
            }
        });

    }

}