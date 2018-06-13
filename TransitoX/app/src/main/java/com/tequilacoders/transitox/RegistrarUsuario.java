package com.tequilacoders.transitox;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.tequilacoders.transitox.utilerias.Mensajes;
import com.tequilacoders.transitox.ws.HttpUtils;
import com.tequilacoders.transitox.ws.Response;
import com.tequilacoders.transitox.ws.pojos.Conductor;
import com.tequilacoders.transitox.ws.pojos.Mensaje;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistrarUsuario extends AppCompatActivity {
    private Conductor nuevo;
    private Response resws;

    private EditText txt_licencia;
    private EditText txt_nombre;
    private EditText txt_fecha;
    private EditText txt_cel;
    private EditText txt_con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        txt_licencia = (EditText) findViewById(R.id.txt_licencia);
        txt_nombre = (EditText) findViewById(R.id.txt_nombre);
        txt_fecha = (EditText) findViewById(R.id.txt_fecha);
        txt_cel = (EditText) findViewById(R.id.txt_cel);
        txt_con = (EditText) findViewById(R.id.txt_con);
    }

    public void registrarme(View v){
        if(validar() && isOnline()){
            nuevo = new Conductor();
            nuevo.setNumLicencia(txt_licencia.getText().toString());
            nuevo.setCelular(txt_cel.getText().toString());
            nuevo.setClave(txt_con.getText().toString());
            nuevo.setNombre(txt_nombre.getText().toString());
            nuevo.setFechaNacimiento(txt_fecha.getText().toString());
            WSPOSTRegistroUsuarioTask task = new WSPOSTRegistroUsuarioTask();
            task.execute(nuevo);
        }
    }

    private boolean validar(){
        boolean ok = true;
        if(txt_licencia.getText() == null || txt_licencia.getText().toString().isEmpty()){
            txt_licencia.setError("Debes introducir tu número de licencia");
            ok = false;
        }
        if(txt_nombre.getText() == null || txt_nombre.getText().toString().isEmpty()){
            txt_nombre.setError("Debes introducir tu nombre");
            ok = false;
        }
        if(txt_fecha.getText() == null || txt_fecha.getText().toString().isEmpty()){
            txt_fecha.setError("Debes introducir el celular de acceso");
            ok = false;
        }
        if(txt_cel.getText() == null || txt_cel.getText().toString().isEmpty()){
            txt_cel.setError("Debes introducir el celular de acceso");
            ok = false;
        }
        if(txt_con.getText() == null || txt_con.getText().toString().isEmpty()){
            txt_con.setError("Debes introducir la contraseña de acceso");
        }
        return ok;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean online = (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected());
        if (!online) {
            Mensajes.mostrarAlertDialog("Sin conexion", "No se encontró ninguna conexión a Internet, conéctate " +
                    "a alguna red para continuar utilizando la aplicación",RegistrarUsuario.this);
        }
        return online;
    }

    class WSPOSTRegistroUsuarioTask extends AsyncTask<Object, String, String> {
        protected String doInBackground(Object ... params){
            try{
                resws = HttpUtils.registro((Conductor) params[0]);
            }catch (RuntimeException ex){};

            return null;
        }

        protected void onPostExecute(String result){
            super.onPostExecute(result);
            resultadoRegistro();
        }
    }

    private void resultadoRegistro(){
        if(resws != null && !resws.isError() && resws.getResult() != null){
            if(resws.getResult().contains("numLicencia")){
                Intent intent = new Intent(this, ConfirmacionActivity.class);
                intent.putExtra("json_usuario", resws.getResult());
                startActivity(intent);
                finish();
            }else{
                System.out.println(resws.getResult());
                Mensaje mensaje = new Gson().fromJson(resws.getResult(), Mensaje.class);
                Mensajes.mostrarAlertDialog((mensaje.isError())?"Error":"Aviso", mensaje.getMensaje(),RegistrarUsuario.this);
            }
        }else{
            Resources res = RegistrarUsuario.this.getResources();
            if (resws == null){
                Drawable icono = res.getDrawable(R.drawable.ic_ghost);
                Mensajes.mostrarAlertDialog("Booh!", "El servidor está muerto",RegistrarUsuario.this,icono);
            } else {
                Drawable icono = res.getDrawable(R.drawable.ic_duck);
                Mensajes.mostrarAlertDialog("¿Ehh?", "Ya existe otro usuario con esas credenciales",RegistrarUsuario.this,icono);
            }

        }
    }


}
