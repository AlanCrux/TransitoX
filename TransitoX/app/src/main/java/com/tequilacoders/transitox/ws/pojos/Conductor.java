
package com.tequilacoders.transitox.ws.pojos;

import java.util.Date;

public class Conductor {
    private String numLicencia;
    private String nombre;
    private String fechaNacimiento;
    private String celular;
    private String clave;
    private String verificado;

    public Conductor() {
    }

    public Conductor(String numLicencia, String nombre, String apPaterno, String apMaterno, String fechaNacimiento, String celular, String clave) {
        this.numLicencia = numLicencia;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.celular = celular;
        this.clave = clave;
    }

    public String getVerificado() {
        return verificado;
    }

    public void setVerificado(String verificado) {
        this.verificado = verificado;
    }


    public String getNumLicencia() {
        return numLicencia;
    }

    public void setNumLicencia(String numLicencia) {
        this.numLicencia = numLicencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}