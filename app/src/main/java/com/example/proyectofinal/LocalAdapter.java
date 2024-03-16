package com.example.proyectofinal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Modelo.Local;
import VistaModelo.VMLocal;

public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.ViewHolder> {

    Context context;
    VMLocal vmLocal;
    ArrayList<Local> localesEspecíficos = new ArrayList<>();
    ArrayList<Local> listaLocales;
    LayoutInflater layoutInflater = null;
    public LocalAdapter(Context context, VMLocal vmLocal) {
        this.context = context;
        this.vmLocal = vmLocal;
        localesEspecíficos = null;
        listaLocales = new ArrayList<>();
        listaLocales.addAll(vmLocal.ListasE());
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public LocalAdapter(Context context, ArrayList<Local> lista) {
        this.context = context;
        this.vmLocal = null;
        this.localesEspecíficos = lista;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Atributo para realizar el click
    OnLocalClickListener onLocalClickListener;

    public interface OnLocalClickListener {
        void OnLocalClick(Local local, int llamada);
    }

    public void setOnLocalClickListener(OnLocalClickListener onLocalClickListener) {
        this.onLocalClickListener = onLocalClickListener;
    }

    @NonNull
    @Override
    public LocalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.local_item_lis, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull LocalAdapter.ViewHolder holder, int position) {
        Local local;
        if (vmLocal == null) {
            local = localesEspecíficos.get(position);
        } else {
            local = vmLocal.ObtenerLocal(position);
        }
        holder.tvnombre.setText(local.getNombre());
        holder.tvPrecio.setText("S/. " + local.getPrecio());
        holder.tvDisponibilidad.setText(local.getDisponibildiad());
        holder.tvUbicacion.setText(local.getUbicacion());
        holder.ivLocal.setImageResource(DecodificarBytesToInt(local.getImagen()));

        holder.bReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean usuarioRegistrado = verificarUsuarioRegistrado();
                if(usuarioRegistrado) {
                    onLocalClickListener.OnLocalClick(local, 1);
                } else {
                    mostrarVentanaRegistro();
                }
            }
        });
    }

    private boolean verificarUsuarioRegistrado() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null;
    }

    private void mostrarVentanaRegistro() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Para reservar, primero debes iniciar sesión o registrarte.")
                .setPositiveButton("Registrarse", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Abrir actividad de registro
                        Intent intent = new Intent(context, RegistrarActivity.class);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // No hacer nada, simplemente cerrar el diálogo
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        if (vmLocal == null) {
            return localesEspecíficos.size();
        }
        return vmLocal.sizeLocal();
    }


    private int DecodificarBytesToInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getInt();
    }
    public void filtrado(String texto) {
        if (texto.isEmpty()) {
            vmLocal.listaLocales.addAll(vmLocal.ListasE());
        } else {
            List<Local> collecion = vmLocal.listaLocales.stream().filter(i -> i.getNombre().toLowerCase().contains(texto.toLowerCase())).collect(Collectors.toList());
            vmLocal.listaLocales.clear();
            vmLocal.listaLocales.addAll(collecion);
        }
        notifyDataSetChanged();//para notificar que hemos hechos cambios
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvnombre, tvUbicacion, tvPrecio, tvDisponibilidad;
        ImageView ivLocal;
        Button bReservar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvnombre = itemView.findViewById(R.id.tv_nombre);
            tvUbicacion = itemView.findViewById(R.id.tv_ubicacion);
            tvPrecio = itemView.findViewById(R.id.tv_precio);
            tvDisponibilidad = itemView.findViewById(R.id.tv_disponibilidad);
            ivLocal = itemView.findViewById(R.id.iv_local);
            bReservar = itemView.findViewById(R.id.b_reservarItem);
        }
    }
}
