
package com.tequilacoders.transitox.ws.pojos;

import java.util.Date;

public class Reporte {
    private String folioReporte;
    private float latitud;
    private float longitud;
    private String nombreConductor;
    private Date fechaHora;

    public String getFolioReporte() {
        return folioReporte;
    }

    public void setFolioReporte(String folioReporte) {
        this.folioReporte = folioReporte;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public String getNombreConductor() {
        return nombreConductor;
    }

    public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }
}
