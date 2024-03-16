package com.example.proyectofinal;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Modelo.Reserva;
import VistaModelo.VMReserva;

public class ReservasAdapter extends RecyclerView.Adapter<ReservasAdapter.ViewHolder> {
    Context context;
    VMReserva vmReserva;
    LayoutInflater layoutInflater = null;
    Activity activity;

    public ReservasAdapter(Context context, VMReserva vmReserva) {
        this.context = context;
        this.vmReserva = vmReserva;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    OnReservaClickListener onReservaClickListener;

    public interface OnReservaClickListener {
        void onReservaClick(Reserva reserva, int llamada);
    }

    public void setOnReservaClickListener(OnReservaClickListener onReservaClickListener) {
        this.onReservaClickListener = onReservaClickListener;
    }

    @NonNull
    @Override
    public ReservasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reserva_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservasAdapter.ViewHolder holder, int position) {
        Reserva reserva = vmReserva.ObtenerReserva(position);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String fechaInicio = sdf.format(reserva.getFechaInicio());
        String fechaFin = sdf.format(reserva.getFechaFinal());

        holder.tvnombreReservado.setText(reserva.getLocalID().getNombre());
        holder.tvUbicacionReservada.setText(reserva.getLocalID().getUbicacion());
        holder.tvPrecioReserva.setText("s/. " + reserva.getCosto());
        holder.tvFechaReservada.setText(fechaInicio + " hasta " + fechaFin);
        holder.tvDescripcionR.setText(reserva.getDescripcionActi());
        holder.ivLocalReservado.setImageResource(DecodificarBytesToInt(reserva.getLocalID().getImagen()));

        holder.bModificarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReservaClickListener.onReservaClick(reserva, 1);
            }
        });
        holder.bEliminarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onReservaClickListener!= null){
                    onReservaClickListener.onReservaClick(reserva, 2);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return vmReserva.sizeReservas();
    }

    private int DecodificarBytesToInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getInt();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvnombreReservado, tvUbicacionReservada, tvPrecioReserva, tvFechaReservada, tvDescripcionR;
        ImageView ivLocalReservado;
        Button bModificarReserva, bEliminarReserva;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvnombreReservado = itemView.findViewById(R.id.tv_nombreReservado);
            tvUbicacionReservada = itemView.findViewById(R.id.tv_ubicacionReservada);
            tvPrecioReserva = itemView.findViewById(R.id.tv_precioReserva);
            tvFechaReservada = itemView.findViewById(R.id.tv_fechaReservada);
            tvDescripcionR = itemView.findViewById(R.id.tv_descripcionR);
            ivLocalReservado = itemView.findViewById(R.id.iv_localReservado);
            bModificarReserva = itemView.findViewById(R.id.b_modificarReser);
            bEliminarReserva = itemView.findViewById(R.id.b_eliminarReserva);

        }
    }
}