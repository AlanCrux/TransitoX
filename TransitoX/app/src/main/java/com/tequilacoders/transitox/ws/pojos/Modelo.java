
package com.tequilacoders.transitox.ws.pojos;


import java.sql.Blob;

public class Modelo {
    private int idModelo;
    String nombre;
    Blob foto;
    private int idMarca;

    public Modelo() {
    }

    public Modelo(int idModelo, String nombre, Blob foto, int idMarca) {
        this.idModelo = idModelo;
        this.nombre = nombre;
        this.foto = foto;
        this.idMarca = idMarca;
    }

    public int getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(int idModelo) {
        this.idModelo = idModelo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Blob getFoto() {
        return foto;
    }

    public void setFoto(Blob foto) {
        this.foto = foto;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }
    
    
    
}
