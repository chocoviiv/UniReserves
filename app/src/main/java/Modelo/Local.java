package Modelo;

import java.io.Serializable;

public class Local implements Serializable {
    private String nombre;
    private Categoria categoria;
    private byte[] imagen;
    private double precio;
    private String Ubicacion;
    private String Disponibildiad;

    public Local(String nombre, Categoria categoria, byte[] imagen, double precio, String ubicacion, String disponibildiad) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.imagen = imagen;
        this.precio = precio;
        Ubicacion = ubicacion;
        Disponibildiad = disponibildiad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        Ubicacion = ubicacion;
    }

    public String getDisponibildiad() {
        return Disponibildiad;
    }

    public void setDisponibildiad(String disponibildiad) {
        Disponibildiad = disponibildiad;
    }

    @Override
    public String toString() {
        return "Local{" +
                "nombre='" + nombre + '\'' +
                ",categoría =" + categoria.getNombreCategoria() +
                ", imagen=" + imagen +
                ", precio=" + precio +
                ", Ubicacion='" + Ubicacion + '\'' +
                ", Disponibildiad='" + Disponibildiad + '\'' +
                '}';
    }
}
