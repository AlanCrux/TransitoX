
package com.tequilacoders.transitox.ws.pojos;

public class Empleado {
    private int idEmpleado;
    private String nombre;
    private String apPaterno;
    private String apMaterno;

    public Empleado() {
    }

    public Empleado(int idEmpleado, String nombre, String apPaterno, String apMaterno) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }
    
    
}
