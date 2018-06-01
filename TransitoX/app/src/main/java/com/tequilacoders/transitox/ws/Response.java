package com.tequilacoders.transitox.ws;

/**
 * Created by alancrux on 06/04/18.
 */

public class Response {

    private boolean error;
    private int status;
    private String result;

    public Response(){}

    public Response(int status, String result, boolean error) {
        this.status = status;
        this.result = result;
        this.error = error;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}