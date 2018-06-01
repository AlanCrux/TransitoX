
package com.tequilacoders.transitox.ws.pojos;

public class Aseguradora {
    private int idAseguradora;
    private String nombre;
    private String numeroContrato;

    public Aseguradora() {
    }

    public Aseguradora(int idAseguradora, String nombre, String numeroContrato) {
        this.idAseguradora = idAseguradora;
        this.nombre = nombre;
        this.numeroContrato = numeroContrato;
    }

    public int getIdAseguradora() {
        return idAseguradora;
    }

    public void setIdAseguradora(int idAseguradora) {
        this.idAseguradora = idAseguradora;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(String numeroContrato) {
        this.numeroContrato = numeroContrato;
    }
    
    
}
