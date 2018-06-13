package com.tequilacoders.transitox;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tequilacoders.transitox.utilerias.Mensajes;
import com.tequilacoders.transitox.ws.HttpUtils;
import com.tequilacoders.transitox.ws.Response;
import com.tequilacoders.transitox.ws.pojos.Aseguradora;
import com.tequilacoders.transitox.ws.pojos.Conductor;
import com.tequilacoders.transitox.ws.pojos.Reporte;
import com.tequilacoders.transitox.ws.pojos.Vehiculo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LevantarReporteActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private double latitud;
    private double longitud;
    private Conductor conductor;
    private Response resws;
    private List<Vehiculo> lista_vehiculos;

    private TextView lbl_conductor;
    private Spinner spinner_vehiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levantar_reporte);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        lbl_conductor = (TextView) findViewById(R.id.lbl_conductor);
        spinner_vehiculo = (Spinner) findViewById(R.id.spinner_vehiculo);
        parametrosIntent();
        cargarVehiculos();
    }



    private void cargarVehiculos(){
        if (isOnline()) {
            WSGETVehiculosTask task = new WSGETVehiculosTask();
            task.execute(conductor.getNumLicencia());
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean online = (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected());
        if (!online) {
            Mensajes.mostrarAlertDialog("No hay conexión a Internet","Conectate a alguna red para continuar usando la aplicación",LevantarReporteActivity.this);
        }
        return online;
    }

    private void parametrosIntent(){
        Intent intent = getIntent();
        String json = intent.getStringExtra("json_usuario");
        Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        conductor = gson.fromJson(json, Conductor.class);
        lbl_conductor.setText("Conductor: "+conductor.getNombre());
    }

    //CLASE PARA OBTENER LOS VEHICULOS
    class WSGETVehiculosTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {
            resws = HttpUtils.obtenerVehiculos(params[0]);
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            resultadoVehiculos();
        }
    }

    public void resultadoVehiculos(){
        if (resws != null && !resws.isError() && resws.getResult() != null) {
            Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            lista_vehiculos = gson.fromJson(resws.getResult(),
                    new TypeToken<List<Vehiculo>>() {
                    }.getType());

            List<String> nombres = new ArrayList<>();
            if (lista_vehiculos != null) {
                for (Vehiculo n : lista_vehiculos) {
                    nombres.add(n.toString());
                }
            }
            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, nombres);
            this.spinner_vehiculo.setAdapter(itemsAdapter);
        }else{
            Mensajes.mostrarAlertDialog("Error", resws.getResult(),LevantarReporteActivity.this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationManager lm = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        @SuppressLint("MissingPermission")
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitud = location.getLongitude();
        latitud = location.getLatitude();
        LatLng ubicacion = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(ubicacion).title("Tú ubicación"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, longitud), 18.0f));
    }

    public void onActionSiguiente(View v){
        Intent intent = new Intent(this, LevantarReporteInvolucrado.class);
        intent.putExtra("placas",spinner_vehiculo.getSelectedItem().toString());
        intent.putExtra("longitud",longitud);
        intent.putExtra("latitud",latitud);
        startActivity(intent);
    }
}
