package com.example.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Modelo.Cliente;
import Modelo.Local;
import Modelo.Reserva;
import VistaModelo.VMCliente;
import VistaModelo.VMLocal;
import VistaModelo.VMReserva;

public class ModificarReservaActivity extends AppCompatActivity {

    Button binicio, bReservas, bPerfil, bIngFechaIni, bIngFechaFin, bHacerReserva;
    EditText etFechaIni, etFechaFin, etDescripcion;
    Reserva reserva;

    private int dia, mes, año, hora, minutos;
    String horaInicio = "", horaFin = "";
    String fechaIni = "", fechaFin = "";
    FirebaseAuth auth;
    FirebaseUser user;
    VMCliente vmCliente;
    VMLocal vmLocal;
    VMReserva vmReserva;
    Local local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_reserva);
        //toolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Editar Reserva");
        setSupportActionBar(toolbar);

        //para agregar la flecha de navegar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        vmReserva = new VMReserva();
        vmCliente = new VMCliente();
        vmLocal = new VMLocal(ModificarReservaActivity.this);

        binicio = findViewById(R.id.b_inicioReservarM);
        bReservas = findViewById(R.id.b_reservasReservarM);
        bPerfil = findViewById(R.id.b_perfilReservarM);
        bIngFechaIni = findViewById(R.id.b_modificarFechaIni);
        bIngFechaFin = findViewById(R.id.b_modificarFechaFin);
        bHacerReserva = findViewById(R.id.b_modicarReserva);
        etFechaIni = findViewById(R.id.et_modificarFechaInicial);
        etFechaFin = findViewById(R.id.et_modificarFechaFinal);
        etDescripcion = findViewById(R.id.et_modificarDescripcion);
        etFechaIni.setEnabled(false);
        etFechaFin.setEnabled(false);
        etFechaIni.setTextColor(getResources().getColor(R.color.black));
        etFechaFin.setTextColor(getResources().getColor(R.color.black));


        Bundle datoReserva = getIntent().getExtras();
        reserva = (Reserva) datoReserva.getSerializable("reserva");
        local = reserva.getLocalID();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String fechaInicio = sdf.format(reserva.getFechaInicio());
        String fechaFin = sdf.format(reserva.getFechaFinal());

        etFechaIni.setText(fechaInicio);
        etFechaFin.setText(fechaFin);
        etDescripcion.setText(reserva.getDescripcionActi());

        bIngFechaIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObtenerFechaIni();
            }
        });

        bIngFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObtenerFechaFinal();
            }
        });

        //Modifica reserva
        bHacerReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etFechaIni.getText().toString().isEmpty() || etFechaFin.getText().toString().isEmpty() || etDescripcion.getText().toString().isEmpty()) {
                    Toast.makeText(ModificarReservaActivity.this, "Llene las fechas", Toast.LENGTH_SHORT).show();
                } else {
                    ModificarReserva();
                }
            }
        });

        binicio.setOnClickListener(v -> {
            Intent intent = new Intent(ModificarReservaActivity.this, MainActivity.class);
            startActivity(intent);
        });

        bReservas.setOnClickListener(v -> {
            Intent intent = new Intent(ModificarReservaActivity.this, ReservasActivity.class);
            startActivity(intent);
        });

        bPerfil.setOnClickListener(v -> {
            Intent intent;
            intent = new Intent(ModificarReservaActivity.this, PerfilActivity.class);
            startActivity(intent);
        });
    }

    public void ModificarReserva() {
        int idReserva = 0;
        Cliente cliente = null;
        if (user != null) {
            cliente = vmCliente.ClienteCorreo(ModificarReservaActivity.this, user.getEmail());
            idReserva = vmReserva.ObtenerIdReserva(ModificarReservaActivity.this,user.getEmail());
        }

        // Suponiendo que tienes las cadenas de texto etFechaIni y etFechaFin en el formato "dd/MM/yyyy HH:mm"
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Date fechaInicio = null;
        try {
            fechaInicio = formatter.parse(etFechaIni.getText().toString());

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        // Formatear la fecha en el formato deseado
        String fechaFormateada = formatter.format(fechaInicio);
        Date fechaFin = null;
        try {
            fechaFin = formatter.parse(etFechaFin.getText().toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (idReserva > 0) {
            Reserva reserva = new Reserva(cliente, local, etDescripcion.getText().toString(), fechaInicio, fechaFin, local.getPrecio());
            if (vmReserva.ModificarReserva(ModificarReservaActivity.this, reserva, idReserva)) {
                Toast.makeText(this, "Reserva modificada exitosamente.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ModificarReservaActivity.this, ReservasActivity.class));
            } else {
                Toast.makeText(this, "Fallo al modificar", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void ObtenerFechaIni() {
        int idLocal = 0;
        if (user != null) {
            idLocal = vmLocal.ObtenerIdLocal(ModificarReservaActivity.this, local.getNombre());
        }
        dia = 0;
        mes = 0;
        año = 0;
        Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        año = c.get(Calendar.YEAR);

        // Establecer el límite máximo como la fecha actual
        long fechaActualMillis = c.getTimeInMillis();

        // Configurar el idioma de la aplicación
        Locale idioma = Locale.getDefault();
        Locale.setDefault(idioma);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ModificarReservaActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fechaIni = dayOfMonth + "/" + (month + 1) + "/" + year;
                etFechaIni.setText(fechaIni + " " + horaInicio);
                ObtenerHoraIni();
            }
        }, año, mes, dia);


        ArrayList<Date> fechasReservadas = vmReserva.obtenerFechasReservadas(ModificarReservaActivity.this, idLocal);

        // Establecer el límite mínimo como la fecha actual
        datePickerDialog.getDatePicker().setMinDate(fechaActualMillis);

        // Deshabilitar las fechas reservadas en el calendario
        datePickerDialog.getDatePicker().setMinDate(fechaActualMillis); // Establecer la fecha mínima como la fecha actual
        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.add(Calendar.YEAR, 2); // Establecer la fecha máxima como un año después de la fecha actual
        datePickerDialog.getDatePicker().setMaxDate(maxCalendar.getTimeInMillis());

        // Crear un ArrayList de Long para las fechas reservadas
        ArrayList<Long> fechasReservadasMillis = new ArrayList<>();
        for (Date fechaReservada : fechasReservadas) {
            fechasReservadasMillis.add(fechaReservada.getTime());
        }

        // Deshabilitar las fechas reservadas en el calendario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePickerDialog.getDatePicker().setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year, monthOfYear, dayOfMonth);
                    if (fechasReservadasMillis.contains(selectedCalendar.getTimeInMillis())) {
                        // La fecha seleccionada está reservada, mostrar un mensaje de error
                        Toast.makeText(ModificarReservaActivity.this, "Esta fecha ya está reservada", Toast.LENGTH_SHORT).show();
                        // Restablecer la fecha seleccionada
                        view.updateDate(año, mes, dia);
                    }
                }
            });
        }
        datePickerDialog.show();
    }

    public void ObtenerHoraIni() {
        Calendar c = Calendar.getInstance();
        hora = c.get(Calendar.HOUR_OF_DAY);
        minutos = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(ModificarReservaActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String minutosString = (minute < 10) ? "0" + minute : String.valueOf(minute);
                horaInicio = hourOfDay + ":" + minutosString;
                etFechaIni.setText(fechaIni + " " + horaInicio);
            }
        }, hora, minutos, false);
        timePickerDialog.show();
    }

    public void ObtenerFechaFinal() {
        dia = 0;
        mes = 0;
        año = 0;
        Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        año = c.get(Calendar.YEAR);

        // Establecer el límite máximo como la fecha actual
        long fechaActualMillis = c.getTimeInMillis();

        DatePickerDialog datePickerDialog = new DatePickerDialog(ModificarReservaActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fechaFin = dayOfMonth + "/" + (month + 1) + "/" + year;
                etFechaFin.setText(fechaFin + " " + horaFin);
                ObtenerHoraFin();
            }
        }, año, mes, dia);

        // Establecer el límite mínimo como la fecha actual
        datePickerDialog.getDatePicker().setMinDate(fechaActualMillis);
        datePickerDialog.show();
    }

    public void ObtenerHoraFin() {
        hora = 0;
        minutos = 0;
        Calendar c = Calendar.getInstance();
        hora = c.get(Calendar.HOUR_OF_DAY);
        minutos = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(ModificarReservaActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String minutosString = (minute < 10) ? "0" + minute : String.valueOf(minute);
                horaFin = hourOfDay + ":" + minutosString;
                etFechaFin.setText(fechaFin + " " + horaFin);
            }
        }, hora, minutos, false);
        timePickerDialog.show();
    }


}