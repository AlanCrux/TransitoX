
package com.tequilacoders.transitox.ws.pojos;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Reporte {

    private int folioReporte;
    private float latitud;
    private float longitud;
    private String nombreConductor2;
    private Date fechaHora;
    private String placas;
    private String marca;
    private String modelo;
    private String color;
    private String placasInvolucrado;
    private String numPoliza;
    private String nombreAseguradora;

    public String getNumPoliza() {
        return numPoliza;
    }

    public void setNumPoliza(String numPoliza) {
        this.numPoliza = numPoliza;
    }

    public String getNombreAseguradora() {
        return nombreAseguradora;
    }

    public void setNombreAseguradora(String nombreAseguradora) {
        this.nombreAseguradora = nombreAseguradora;
    }



    public int getFolioReporte() {
        return folioReporte;
    }

    public void setFolioReporte(int folioReporte) {
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

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getNombreConductor2() {
        return nombreConductor2;
    }

    public void setNombreConductor2(String nombreConductor2) {
        this.nombreConductor2 = nombreConductor2;
    }

    public String getPlacas() {
        return placas;
    }

    public void setPlacas(String placas) {
        this.placas = placas;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPlacasInvolucrado() {
        return placasInvolucrado;
    }

    public void setPlacasInvolucrado(String placasInvolucrado) {
        this.placasInvolucrado = placasInvolucrado;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fecha = sdf.format(fechaHora);
        return "Folio: "+ folioReporte + " Fecha: "+fecha;
    }
}
