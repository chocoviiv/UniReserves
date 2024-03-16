package Modelo;

import java.io.Serializable;

public class Cliente implements Serializable {
    private  String Id;
    private String nombres;
    private String apellidos;
    private String celular;
    private String dni;
    private String direccion;
    private String tipo;
    private byte[] imagen;
    private String correo;
    private String contraseña;

    public Cliente(String nombres, String apellidos, String celular, String dni, String direccion, String correo, String contraseña) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.celular = celular;
        this.dni = dni;
        this.direccion = direccion;
        if (esUnc(correo)) {
            this.tipo = "Es de la UNC";
        } else {
            this.tipo = "Externo a la UNC";
        }
        this.imagen = null;
        this.correo = correo;
        this.contraseña = contraseña;
    }

    public Cliente(String nombres, String apellidos, String celular, String dni, String direccion, byte[] imagen, String correo, String contraseña) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.celular = celular;
        this.dni = dni;
        this.direccion = direccion;
        if (esUnc(correo)) {
            this.tipo = "Miembro de la UNC";
        } else {
            this.tipo = "Externo a la UNC";
        }
        this.imagen = imagen;
        this.correo = correo;
        this.contraseña = contraseña;
    }

    public boolean esUnc(String correo) {
        if (correo.matches(".*@unc\\.edu\\.pe")) {
            return true;
        }
        return false;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", celular='" + celular + '\'' +
                ", dni='" + dni + '\'' +
                ", direccion='" + direccion + '\'' +
                ", tipo='" + tipo + '\'' +
                ", correo='" + correo + '\'' +
                ", contraseña='" + contraseña + '\'' +
                '}';
    }
}
