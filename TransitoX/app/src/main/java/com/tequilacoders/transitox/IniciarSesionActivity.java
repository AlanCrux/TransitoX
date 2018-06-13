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
import com.google.gson.GsonBuilder;
import com.tequilacoders.transitox.utilerias.Mensajes;
import com.tequilacoders.transitox.ws.HttpUtils;
import com.tequilacoders.transitox.ws.Response;
import com.tequilacoders.transitox.ws.pojos.Conductor;
import com.tequilacoders.transitox.ws.pojos.Mensaje;


public class IniciarSesionActivity extends AppCompatActivity {
    private String celular;
    private String contrasena;
    private EditText txt_cel;
    private EditText txt_con;
    private Response resws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        txt_cel = (EditText) findViewById(R.id.txt_cel);
        txt_con = (EditText) findViewById(R.id.txt_con);
    }

    public void entrar(View v) {
        if (validar() && isOnline()) {
            celular = txt_cel.getText().toString();
            contrasena = txt_con.getText().toString();
            WSPOSTLoginTask task = new WSPOSTLoginTask();
            task.execute(celular, contrasena);
        }
    }

    public boolean validar() {
        boolean ok = true;
        if (txt_cel.getText() == null || txt_cel.getText().toString().isEmpty()) {
            txt_cel.setError("Debes introducir el número de celular");
            ok = false;
        }
        if (txt_con.getText() == null || txt_con.getText().toString().isEmpty()) {
            txt_con.setError("Debes introducir la contraseña de acceso");
            ok = false;
        }
        return ok;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean online = (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected());
        if (!online) {
            Mensajes.mostrarAlertDialog("Sin conexion", "No se encontró ninguna conexión a Internet, conéctate " +
                    "a alguna red para continuar utilizando la aplicación", IniciarSesionActivity.this);
        }
        return online;
    }

    class WSPOSTLoginTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String ... params){
            try{
                resws = HttpUtils.login(params[0], params[1]);
            } catch (RuntimeException ex){};
            return null;
        }

        protected void onPostExecute(String result){
            super.onPostExecute(result);
            resultadoEntrar();
        }
    }

    private void resultadoEntrar(){
        if(resws != null && !resws.isError() && resws.getResult() != null){
            if(resws.getResult().contains("numLicencia")){
                Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                Conductor conductor = gson.fromJson(resws.getResult(), Conductor.class);
                if (conductor.getVerificado() != null){
                    if (conductor.getVerificado().equals("True")){
                        Intent intent = new Intent(this, PrincipalActivity.class);
                        intent.putExtra("json_usuario", resws.getResult());
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(this, ConfirmacionActivity.class);
                        intent.putExtra("json_usuario", resws.getResult());
                        startActivity(intent);
                    }
                } else{
                    Intent intent = new Intent(this, ConfirmacionActivity.class);
                    intent.putExtra("json_usuario", resws.getResult());
                    startActivity(intent);
                }
            }else{
                Mensaje mensaje = new Gson().fromJson(resws.getResult(), Mensaje.class);
                Mensajes.mostrarAlertDialog((mensaje.isError()) ? "Error" : "Aviso", mensaje.getMensaje(),IniciarSesionActivity.this);
            }
        }else{
            Resources res = IniciarSesionActivity.this.getResources();
            if (resws == null){
                Drawable icono = res.getDrawable(R.drawable.ic_ghost);
                Mensajes.mostrarAlertDialog("Booh!", "El servidor está muerto",IniciarSesionActivity.this,icono);
            } else {
                Drawable icono = res.getDrawable(R.drawable.ic_duck);
                Mensajes.mostrarAlertDialog("¿Ehh?", "Tus credenciales no son válidas",IniciarSesionActivity.this,icono);
            }

        }
    }
}
