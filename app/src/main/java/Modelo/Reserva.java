package Modelo;

import com.google.type.DateTime;

import java.io.Serializable;
import java.util.Date;

public class Reserva implements Serializable {
    private Cliente clienteID;
    private Local localID;
    private Date fechaInicio;
    private Date fechaFinal;
    private String descripcionActi;
    private double costo;

    public Reserva(Cliente clienteID, Local localID, String descripcionActi, Date fechaInicio, Date fechaFinal, double costo) {
        this.clienteID = clienteID;
        this.localID = localID;
        this.descripcionActi = descripcionActi;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.costo = costo;
    }

    public Cliente getClienteID() {
        return clienteID;
    }

    public void setClienteID(Cliente clienteID) {
        this.clienteID = clienteID;
    }

    public Local getLocalID() {
        return localID;
    }

    public void setLocalID(Local localID) {
        this.localID = localID;
    }

    public String getDescripcionActi() {
        return descripcionActi;
    }

    public void setDescripcionActi(String descripcionActi) {
        this.descripcionActi = descripcionActi;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "clienteID=" + clienteID +
                ", localID=" + localID +
                ", fechaInicio=" + fechaInicio +
                ", fechaFinal=" + fechaFinal +
                ", precio=" + costo +
                '}';
    }

}