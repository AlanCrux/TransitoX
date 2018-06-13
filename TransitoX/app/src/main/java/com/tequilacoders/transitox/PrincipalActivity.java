package com.tequilacoders.transitox;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tequilacoders.transitox.utilerias.Mensajes;
import com.tequilacoders.transitox.ws.HttpUtils;
import com.tequilacoders.transitox.ws.Response;
import com.tequilacoders.transitox.ws.pojos.Conductor;
import com.tequilacoders.transitox.ws.pojos.Reporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {
    private Conductor conductor;
    private Reporte seleccionado;

    private ListView lst_reportes;
    private TextView lbl_conductor;
    private List<Reporte> lista_reportes;
    private Response resws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        lbl_conductor = (TextView) findViewById(R.id.lbl_conductor);
        lst_reportes = (ListView) findViewById(R.id.lst_reportes);
        lst_reportes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mostrarDictamen(i);
            }
        });

        parametrosIntent();
        cargarReportes();
    }

    private void parametrosIntent(){
        Intent intent = getIntent();
        String json = intent.getStringExtra("json_usuario");
        Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        conductor = gson.fromJson(json, Conductor.class);
        lbl_conductor.setText(conductor.getNombre());
        Toast.makeText(this, "Bienvenido: " + conductor.getNombre(), Toast.LENGTH_SHORT).show();
    }

    private void cargarReportes(){
        if (isOnline()) {
            WSGETReportesTask task = new WSGETReportesTask();
            task.execute(conductor.getNumLicencia());
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean online = (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected());
        if (!online) {
            Mensajes.mostrarAlertDialog("No hay conexión a Internet","Conectate a alguna red para continuar usando la aplicación",PrincipalActivity.this);
        }
        return online;
    }


    public void onActionLevantarReporte(View v) {
        Intent intent = new Intent(this, LevantarReporteActivity.class);
        Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        intent.putExtra("json_usuario", gson.toJson(conductor));
        startActivity(intent);
    }

    public void onActionAjustes(View v) {
        Intent intent = new Intent(this, ListaVehiculos.class);
        Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        intent.putExtra("json_usuario", gson.toJson(conductor));
        startActivity(intent);
    }

    public void onFinish(View v){
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        cargarReportes();
    }

    private void mostrarDictamen(int index){
        seleccionado = this.lista_reportes.get(index);
        Intent i = new Intent(this, DetalleReporte.class);
        i.putExtra("r_id", seleccionado.getFolioReporte());
        startActivity(i);
    }

    //CLASE PARA OBTENER LOS REPORTES
    class WSGETReportesTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {
            resws = HttpUtils.obtenerReportes(params[0]);
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            resultadoReportes();
        }
    }

    private void resultadoReportes() {
        if (resws != null && !resws.isError() && resws.getResult() != null) {
            Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            lista_reportes = gson.fromJson(resws.getResult(),
                    new TypeToken<List<Reporte>>() {
                    }.getType());

            List<String> nombres = new ArrayList<>();
            if (lista_reportes != null) {
                for (Reporte n : lista_reportes) {
                    nombres.add(n.toString());
                }
            }
            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, nombres);
            this.lst_reportes.setAdapter(itemsAdapter);
        }else{
            Mensajes.mostrarAlertDialog("Error", resws.getResult(),PrincipalActivity.this);
        }
    }
}
