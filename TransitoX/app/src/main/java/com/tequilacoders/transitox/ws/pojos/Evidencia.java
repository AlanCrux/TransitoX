
package com.tequilacoders.transitox.ws.pojos;

import java.sql.Blob;
import java.util.Date;

public class Evidencia {
    private int idEvidencia;
    private Blob fotografia;
    private Date fechaHora;

    public Evidencia() {
    }

    public Evidencia(int idEvidencia, Blob fotografia, Date fechaHora) {
        this.idEvidencia = idEvidencia;
        this.fotografia = fotografia;
        this.fechaHora = fechaHora;
    }

    public int getIdEvidencia() {
        return idEvidencia;
    }

    public void setIdEvidencia(int idEvidencia) {
        this.idEvidencia = idEvidencia;
    }

    public Blob getFotografia() {
        return fotografia;
    }

    public void setFotografia(Blob fotografia) {
        this.fotografia = fotografia;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }
    
    
}
