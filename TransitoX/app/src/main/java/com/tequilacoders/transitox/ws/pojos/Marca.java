/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tequilacoders.transitox.ws.pojos;


import java.sql.Blob;

/**
 *
 * @author Esmeralda
 */
public class Marca {
    private int idMArca;
    private String nombre;
    Blob logo;

    public Marca() {
    }

    public Marca(int idMArca, String nombre, Blob logo) {
        this.idMArca = idMArca;
        this.nombre = nombre;
        this.logo = logo;
    }

    public int getIdMArca() {
        return idMArca;
    }

    public void setIdMArca(int idMArca) {
        this.idMArca = idMArca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Blob getLogo() {
        return logo;
    }

    public void setLogo(Blob logo) {
        this.logo = logo;
    }
    
    
    
}
