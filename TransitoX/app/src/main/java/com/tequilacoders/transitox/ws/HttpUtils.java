package com.tequilacoders.transitox.ws;
import android.graphics.Bitmap;
import android.util.Log;

import com.tequilacoders.transitox.ws.pojos.Codigo;
import com.tequilacoders.transitox.ws.pojos.Conductor;
import com.tequilacoders.transitox.ws.pojos.Reporte;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;

public class HttpUtils {

    private static final String BASE_URL = "http://192.168.43.21" +
            "4:8080/TransitoWS/ws/generic/";
    private static final Integer CONNECT_TIMEOUT = 4000; //MILISEGUNDOS
    private static final Integer READ_TIMEOUT = 10000; //MILISEGUNDOS

    public static Response confirmarCodigo(Codigo codigo) {
        //----CREAR PARÁMETROS PARA POST, PUT, DELETE----//
        String param = String.format("telefono=%s&mensaje=%s", codigo.getTelefono(),codigo.getMensaje());
        return invocarServicioWeb("autenticarmensaje","POST",param);
    }

    public static Response obtenerReportes(String numLicencia) {
        String param = String.format("/%s", numLicencia);
        return invocarServicioWeb("obtenerreportes","GET",param);
    }

    public static Response login(String celular, String clave) {
        //----CREAR PARÁMETROS PARA POST, PUT, DELETE----//
        String param = String.format("celular=%s&clave=%s", celular,clave);
        return invocarServicioWeb("autenticarusuario","POST",param);
    }

    public static Response enviar(String celular) {
        //----CREAR PARÁMETROS PARA POST, PUT, DELETE----//
        String param = String.format("numero=%s", celular);
        return invocarServicioWeb("Enviar","POST",param);
    }

    public static Response registro(Conductor conductor) {
        String numLicencia = conductor.getNumLicencia();
        String nombre = conductor.getNombre();
        String fechaNacimiento = conductor.getFechaNacimiento();
        String celular = conductor.getCelular();
        String clave = conductor.getClave();

        String param = String.format(
                "numLicencia=%s&nombre=%s&fechaNacimiento=%s&celular=%s&clave=%s",
                numLicencia, nombre, fechaNacimiento, celular, clave);
        return invocarServicioWeb("registrarusuario", "POST", param);
    }

    public static Response obtenerVehiculos(String numLicencia){
        String param = String.format("/%s", numLicencia);
        return invocarServicioWeb("obtenervehiculos","GET",param);
    }

    public static Response subirFoto(String id, Bitmap bitmap) {
        Response res = new Response();
        HttpURLConnection c = null;
        DataOutputStream outputStream = null;

        try {
            URL url = new URL(BASE_URL+"subir/"+id);
            c = (HttpURLConnection) url.openConnection();
            c.setDoInput(true);
            c.setDoOutput(true);
            c.setUseCaches(false);
            c.setRequestMethod("POST");
            c.setRequestProperty("Connection", "Keep-Alive");
            c.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            c.setRequestProperty("Content-Type", "application/octet-stream;");
            //----------MANDAR BYTES A WS-------------//
            outputStream = new DataOutputStream(c.getOutputStream());
            ByteArrayOutputStream bitmapOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bitmapOutputStream);

            byte original[] = bitmapOutputStream.toByteArray();

            int blockbytes, totalbytes, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 2048 * 2048;

            int lastbyte = 0;
            totalbytes = original.length;
            Log.v("totalbytes",""+totalbytes);
            bufferSize = Math.min(totalbytes, maxBufferSize);
            buffer = Arrays.copyOfRange(original,lastbyte,bufferSize);
            Log.v("copyFromTo","0,"+bufferSize);
            blockbytes = buffer.length;
            Log.v("blockbytes",""+blockbytes);
            while (totalbytes > 0) {
                outputStream.write(buffer, 0, bufferSize);
                totalbytes = totalbytes - blockbytes;
                lastbyte += blockbytes;
                bufferSize = Math.min(totalbytes, maxBufferSize);
                buffer = Arrays.copyOfRange(original,lastbyte,lastbyte+bufferSize);
                blockbytes = buffer.length;
                Log.v("copyFromTo",""+lastbyte+","+bufferSize);
                Log.v("blockbytes",""+blockbytes);
            }
            bitmapOutputStream.close();
            outputStream.flush();
            outputStream.close();

            //----------LEER RESPUESTA DEL WS-----------//

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

    public static Response registrarReporte(Reporte reporte){
        String placas = reporte.getPlacas();
        float latitud = reporte.getLatitud();
        float longitud = reporte.getLongitud();
        String conductor = reporte.getNombreConductor2();
        String marca = reporte.getMarca();
        String modelo = reporte.getModelo();
        String color = reporte.getColor();
        String placasInvolucrado = reporte.getPlacasInvolucrado();
        String nombreAseguradora = reporte.getNombreAseguradora();
        String numPoliza = reporte.getNumPoliza();

        String param = String.format("placas=%s&latitud=%s&longitud=%s&conductor=%s&marca=%s&" +
                        "modelo=%s&color=%s&placasInvolucrado=%s&nombreAseguradora=%s&numPoliza=%s",placas,latitud,
                longitud,conductor,marca,modelo,color,placasInvolucrado,nombreAseguradora,numPoliza );
        return invocarServicioWeb("registrarreporte","POST",param);
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