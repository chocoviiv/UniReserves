package Modelo;

import com.google.type.DateTime;

public class Reserva {
    private Cliente clienteID;
    private Local localID;
    private DateTime fechaInicio;
    private DateTime fechaFinal;
    private String descripcionActi;
    private double costo;

    public Reserva(Cliente clienteID, Local localID, String descripcionActi, DateTime fechaInicio, DateTime fechaFinal, double costo) {
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
        return getDescripcionActi();
    }

    public void setDescripcionActi(String descripcionActi) {
        this.descripcionActi = descripcionActi;
    }

    public DateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(DateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public DateTime getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(DateTime fechaFinal) {
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