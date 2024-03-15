package Modelo;

import java.io.Serializable;

public class Categoria implements Serializable {
    private String idCategoria;
    private String nombreCategoria;

    public Categoria(String idCategoria, String nombreCategoria) {
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "nombreCategoria='" + nombreCategoria + '\'' +
                '}';
    }

}
