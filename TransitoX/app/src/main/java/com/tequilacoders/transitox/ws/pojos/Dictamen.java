
package com.tequilacoders.transitox.ws.pojos;

import java.util.Date;

public class Dictamen {
    private int folioDictamen;
    private Date fechaHora;
    private String descripcion;

    public Dictamen() {
    }

    public Dictamen(int folioDictamen, Date fechaHora, String descripcion) {
        this.folioDictamen = folioDictamen;
        this.fechaHora = fechaHora;
        this.descripcion = descripcion;
    }

    public int getFolioDictamen() {
        return folioDictamen;
    }

    public void setFolioDictamen(int folioDictamen) {
        this.folioDictamen = folioDictamen;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


}
