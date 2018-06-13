package com.tequilacoders.transitox;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tequilacoders.transitox.utilerias.Mensajes;
import com.tequilacoders.transitox.ws.HttpUtils;
import com.tequilacoders.transitox.ws.Response;
import com.tequilacoders.transitox.ws.pojos.Codigo;
import com.tequilacoders.transitox.ws.pojos.Conductor;
import com.tequilacoders.transitox.ws.pojos.Mensaje;

public class ConfirmacionActivity extends AppCompatActivity {
    Conductor conductor;
    EditText txt_codigo;
    private Response resws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacion);
        parametrosIntent();
        txt_codigo = (EditText) findViewById(R.id.txt_codigo);
        txt_codigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_codigo.getText().toString().length() == 4){
                    verificarCodigo();
                }
            }
        });

        WSPOSTEnviarTask task = new WSPOSTEnviarTask();
        task.execute(conductor.getCelular());
    }

    public void verificarCodigo(){
        WSGETConfirmacionTask task = new WSGETConfirmacionTask();
        Codigo codigo = new Codigo();
        codigo.setTelefono(conductor.getCelular());
        codigo.setMensaje(txt_codigo.getText().toString());
        task.execute(codigo);
    }

    class WSGETConfirmacionTask extends AsyncTask<Object, String, String> {
        protected String doInBackground(Object ... params){
            try{
                resws = HttpUtils.confirmarCodigo((Codigo) params[0]);
            }catch (RuntimeException ex){};

            return null;
        }

        protected void onPostExecute(String result){
            super.onPostExecute(result);
            resultadoConfirmacion();
        }
    }

    class WSPOSTEnviarTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String ... params){
            try{
                resws = HttpUtils.enviar((String) params[0]);
            }catch (RuntimeException ex){};

            return null;
        }

        protected void onPostExecute(String result){
            super.onPostExecute(result);
            resultadoEnvio();
        }
    }

    private void resultadoConfirmacion(){
        if(resws != null && !resws.isError() && resws.getResult() != null){
            if(resws.getResult().contains("True")){
                Intent intent = new Intent(this, PrincipalActivity.class);
                Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                intent.putExtra("json_usuario", gson.toJson(conductor));
                startActivity(intent);
                System.out.println(resws.getResult());
            }else{
                Mensaje mensaje = new Gson().fromJson(resws.getResult(), Mensaje.class);
                Mensajes.mostrarAlertDialog((mensaje.isError()) ? "Error" : "Aviso", mensaje.getMensaje(),ConfirmacionActivity.this);
            }
        }else{
            txt_codigo.setError("Código incorrecto");
        }
    }

    private void resultadoEnvio(){

    }

    private void parametrosIntent(){
        Intent intent = getIntent();
        String json = intent.getStringExtra("json_usuario");
        Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        conductor = gson.fromJson(json, Conductor.class);
    }

    public void onReenviarCodigo(View v){
        WSPOSTEnviarTask task = new WSPOSTEnviarTask();
        task.execute(conductor.getCelular());
    }
}
