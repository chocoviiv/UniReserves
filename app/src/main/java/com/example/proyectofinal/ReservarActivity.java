package com.example.proyectofinal;

import androidx.annotation.RequiresApi;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

public class ReservarActivity extends AppCompatActivity {
    Button binicio, bReservas, bPerfil, bIngFechaIni, bIngFechaFin, bHacerReserva;
    EditText etFechaIni, etFechaFin, etDescripcion;

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
        setContentView(R.layout.activity_reservar);

        //toolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Reservar");
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
        vmLocal = new VMLocal(ReservarActivity.this);

        binicio = findViewById(R.id.b_inicioReservar);
        bReservas = findViewById(R.id.b_reservasReservar);
        bPerfil = findViewById(R.id.b_perfilReservar);
        bIngFechaIni = findViewById(R.id.b_ingresarFechaIni);
        bIngFechaFin = findViewById(R.id.b_ingresarFechaFin);
        bHacerReserva = findViewById(R.id.b_hacerReserva);
        etFechaIni = findViewById(R.id.et_fechaInicial);
        etFechaFin = findViewById(R.id.et_fechaFinal);
        etDescripcion = findViewById(R.id.et_descripcion);
        etFechaIni.setEnabled(false);
        etFechaFin.setEnabled(false);
        etFechaIni.setTextColor(getResources().getColor(R.color.black));
        etFechaFin.setTextColor(getResources().getColor(R.color.black));

        Bundle datoLocal = getIntent().getExtras();
        local = (Local) datoLocal.getSerializable("local");

        binicio.setOnClickListener(v -> {
            Intent intent = new Intent(ReservarActivity.this, MainActivity.class);
            startActivity(intent);
        });

        bReservas.setOnClickListener(v -> {
            Intent intent = new Intent(ReservarActivity.this, ReservasActivity.class);
            startActivity(intent);
        });

        bPerfil.setOnClickListener(v -> {
            Intent intent;
            intent = new Intent(ReservarActivity.this, PerfilActivity.class);
            startActivity(intent);
        });

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

        bHacerReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etFechaIni.getText().toString().isEmpty() || etFechaFin.getText().toString().isEmpty() || etDescripcion.getText().toString().isEmpty()) {
                    Toast.makeText(ReservarActivity.this, "Llene las fechas", Toast.LENGTH_SHORT).show();
                } else {
                    AgregarReserva();
                }
            }
        });
    }

    public void AgregarReserva() {
        int idCliente = 0;
        int idLocal = 0;
        Cliente cliente = null;
        if (user != null) {
            idCliente = vmCliente.ObtenerIdCliente(ReservarActivity.this, user.getEmail());
            idLocal = vmLocal.ObtenerIdLocal(ReservarActivity.this, local.getNombre());
            cliente = vmCliente.ClienteCorreo(ReservarActivity.this, user.getEmail());
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

        if (idLocal > 0 && idCliente > 0) {
            Reserva reserva = new Reserva(cliente, local, etDescripcion.getText().toString(), fechaInicio, fechaFin, local.getPrecio());
            if (vmReserva.AgregarReserva(ReservarActivity.this, reserva, idLocal, idCliente)) {
                Toast.makeText(this, "Reserva hecha exitosamente.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ReservarActivity.this, ReservasActivity.class));
            } else {
                Toast.makeText(this, "Fallo al reservar", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void ObtenerFechaIni() {
        int idLocal = 0;
        if (user != null) {
            idLocal = vmLocal.ObtenerIdLocal(ReservarActivity.this, local.getNombre());
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(ReservarActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fechaIni = dayOfMonth + "/" + (month + 1) + "/" + year;
                etFechaIni.setText(fechaIni + " " + horaInicio);
                ObtenerHoraIni();
            }
        }, año, mes, dia);

        ArrayList<Date> fechasReservadas = vmReserva.obtenerFechasReservadas(ReservarActivity.this, idLocal);

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
                        Toast.makeText(ReservarActivity.this, "Esta fecha ya está reservada", Toast.LENGTH_SHORT).show();
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(ReservarActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(ReservarActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(ReservarActivity.this, new TimePickerDialog.OnTimeSetListener() {
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