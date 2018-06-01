package com.tequilacoders.transitox.ws;
import com.tequilacoders.transitox.ws.pojos.Conductor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class HttpUtils {

    private static final String BASE_URL = "http://192.168.1.100:8080/TransitoWS/ws/generic/";
    private static final Integer CONNECT_TIMEOUT = 4000; //MILISEGUNDOS
    private static final Integer READ_TIMEOUT = 10000; //MILISEGUNDOS

    public static Response obtenerNotas(int idUsuario) {
        String param = String.format("/%s", idUsuario);
        return invocarServicioWeb("obtenerNotasPorUsuario","GET",param);
    }

    public static Response login(String celular, String clave) {
        //----CREAR PARÁMETROS PARA POST, PUT, DELETE----//
        String param = String.format("celular=%s&clave=%s", celular,clave);
        return invocarServicioWeb("autenticarusuario","POST",param);
    }

    public static Response registro(Conductor conductor) {
        String numLicencia = conductor.getNumLicencia();
        String nombre = conductor.getNombre();
        String apPaterno = conductor.getApPaterno();
        String apMaterno = conductor.getApMaterno();
        Date fechaNacimiento = conductor.getFechaNacimiento();
        String celular = conductor.getCelular();
        String clave = conductor.getClave();

        String param = String.format(
                "numLicencia=%s&nombre=%s&apPaterno=%s&apMaterno=%s&" +
                "fechaNacimiento=%s&celular=%s&clave=%s",
                numLicencia, nombre, apPaterno, apMaterno, fechaNacimiento, celular, clave);
        return invocarServicioWeb("registrarusuario", "POST", param);
    }

    private static Response invocarServicioWeb(String url, String tipoinvocacion, String parametros){
        HttpURLConnection c = null;
        URL u = null;
        Response res = null;
        try {
            if(tipoinvocacion.compareToIgnoreCase("GET")==0){
                u = new URL(BASE_URL+url+((parametros!=null)?parametros:""));
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod(tipoinvocacion);
                c.setRequestProperty("Content-length", "0");
                c.setUseCaches(false);
                c.setAllowUserInteraction(false);
                c.setConnectTimeout(CONNECT_TIMEOUT);
                c.setReadTimeout(READ_TIMEOUT);
                c.connect();
            } else {
                u = new URL(BASE_URL+url);
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod(tipoinvocacion);
                c.setDoOutput(true);
                c.setConnectTimeout(CONNECT_TIMEOUT);
                c.setReadTimeout(READ_TIMEOUT);
                //----PASAR PARÁMETROS EN EL CUERPO DEL MENSAJE POST, PUT y DELETE----//
                DataOutputStream wr = new DataOutputStream(c.getOutputStream());
                wr.writeBytes(parametros);
                wr.flush();
                wr.close();
                //------------------------------------------------------//
            }
            res = new Response();
            res.setStatus(c.getResponseCode());
            if(res.getStatus()!=200 && res.getStatus()!=201){
                res.setError(true);
            }
            if(c.getInputStream()!=null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();
                res.setResult(sb.toString());
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            res.setError(true);
            res.setResult(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            res.setError(true);
            res.setResult(ex.getMessage());
        } finally {
            if (c != null) {
                c.disconnect();
            }
        }
        return res;
    }

}