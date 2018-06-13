package com.tequilacoders.transitox;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tequilacoders.transitox.utilerias.Mensajes;
import com.tequilacoders.transitox.ws.HttpUtils;
import com.tequilacoders.transitox.ws.Response;
import com.tequilacoders.transitox.ws.pojos.Aseguradora;
import com.tequilacoders.transitox.ws.pojos.Conductor;
import com.tequilacoders.transitox.ws.pojos.Mensaje;
import com.tequilacoders.transitox.ws.pojos.Reporte;
import com.tequilacoders.transitox.ws.pojos.Vehiculo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LevantarReporteInvolucrado extends AppCompatActivity {
    private double latitud;
    private double longitud;
    private String placas;
    private Response resws;
    private static int numFotos;
    private static int numFotosSubidas = 0;
    private Uri photoURI;

    ImageView img_foto1;
    ImageView img_foto2;
    ImageView img_foto3;
    ImageView img_foto4;
    ImageView img_foto5;
    ImageView img_foto6;
    ImageView img_foto7;
    ImageView img_foto8;
    EditText txt_conductor2;
    EditText txt_marca;
    EditText txt_modelo;
    EditText txt_color;
    EditText txt_placas;
    EditText txt_aseguradora;
    EditText txt_poliza;

    ProgressDialog espera;
    List<Bitmap> fotos;

    private Button btn_agregar;
    private Button btn_enviar;

    public static final int REQUEST_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levantar_reporte_involucrado);

        fotos = new ArrayList<>();

        //Mapeando componentes de la IU
        btn_agregar = (Button) findViewById(R.id.btn_agregar);
        btn_enviar = (Button) findViewById(R.id.btn_enviar);
        img_foto1 = (ImageView) findViewById(R.id.img_foto1);
        img_foto2 = (ImageView) findViewById(R.id.img_foto2);
        img_foto3 = (ImageView) findViewById(R.id.img_foto3);
        img_foto4 = (ImageView) findViewById(R.id.img_foto4);
        img_foto5 = (ImageView) findViewById(R.id.img_foto5);
        img_foto6 = (ImageView) findViewById(R.id.img_foto6);
        img_foto6 = (ImageView) findViewById(R.id.img_foto7);
        img_foto8 = (ImageView) findViewById(R.id.img_foto8);
        txt_conductor2 = (EditText) findViewById(R.id.txt_conductor2);
        txt_marca = (EditText) findViewById(R.id.txt_marca);
        txt_modelo = (EditText) findViewById(R.id.txt_modelo);
        txt_color = (EditText) findViewById(R.id.txt_color);
        txt_placas = (EditText) findViewById(R.id.txt_placas);
        txt_aseguradora = (EditText) findViewById(R.id.txt_aseguradora);
        txt_poliza = (EditText) findViewById(R.id.txt_poliza);

        parametrosIntent();
        if(!validarCamara()){
            Toast.makeText(this,"El dispositivo no cuenta con camara",
                    Toast.LENGTH_LONG).show();
        }
        validarPermisosAlmacenamiento();
        btn_enviar.setEnabled(false);
    }

    private void validarPermisosAlmacenamiento() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "SIN ACCESO AL ALMACENAMIENTO INTERNO",
                    Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        }
    }

    private boolean validarCamara(){
        return getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY);
    }

    private void parametrosIntent(){
        Intent intent = getIntent();
        latitud = intent.getDoubleExtra("latitud",0);
        longitud = intent.getDoubleExtra("longitud",0);
        placas = intent.getStringExtra("placas");
    }

    public void tomarFoto(View v){
        numFotos++;
        if (numFotos<4){
            btn_enviar.setEnabled(false);
        } else {
            btn_enviar.setEnabled(true);
        }

        if (numFotos == 8){
            btn_agregar.setEnabled(false);
        } else{
            btn_agregar.setEnabled(true);
        }

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Crear archivo temporal de imagen
        File tmp = null;
        try {
            tmp = crearArchivoTemporal();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (tmp != null) {
            // Acceder a la ruta completa del archivo
            photoURI = FileProvider.getUriForFile(this, "com.tequilacoders.transitox", tmp);
            i.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            // Lanzar camara
            startActivityForResult(i, REQUEST_CAPTURE);
        }
    }

    private File crearArchivoTemporal() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String nombre = sdf.format(new Date()) + ".jpg";
        File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/tmps");
        File archivo = File.createTempFile(nombre, ".jpg",path);
        return archivo;
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        //--------DESDE CAMARA----------//
        if(requestCode == REQUEST_CAPTURE){
            if (resultCode == RESULT_OK) {
                switch (numFotos){
                    case 1:
                        img_foto1.setImageURI(this.photoURI);
                        fotos.add(((BitmapDrawable)img_foto1.getDrawable()).getBitmap());
                        break;
                    case 2:
                        img_foto2.setImageURI(this.photoURI);
                        fotos.add(((BitmapDrawable)img_foto2.getDrawable()).getBitmap());
                        break;
                    case 3:
                        img_foto3.setImageURI(this.photoURI);
                        fotos.add(((BitmapDrawable)img_foto3.getDrawable()).getBitmap());
                        break;
                    case 4:
                        img_foto4.setImageURI(this.photoURI);
                        fotos.add(((BitmapDrawable)img_foto4.getDrawable()).getBitmap());
                        break;
                    case 5:
                        img_foto5.setImageURI(this.photoURI);
                        fotos.add(((BitmapDrawable)img_foto5.getDrawable()).getBitmap());
                        break;
                    case 6:
                        img_foto6.setImageURI(this.photoURI);
                        fotos.add(((BitmapDrawable)img_foto6.getDrawable()).getBitmap());
                        break;
                    case 7:
                        img_foto7.setImageURI(this.photoURI);
                        fotos.add(((BitmapDrawable)img_foto7.getDrawable()).getBitmap());
                        break;
                    case 8:
                        img_foto8.setImageURI(this.photoURI);
                        fotos.add(((BitmapDrawable)img_foto8.getDrawable()).getBitmap());
                        break;
                }
            }
        }
    }

    public void enviarReporte(View v){
        WSPOSTRegistrarReporteTask task = new WSPOSTRegistrarReporteTask();

        if (validar() && isOnline()){
            Reporte reporte = new Reporte();
            reporte.setPlacas(placas);
            reporte.setLatitud((float) latitud);
            reporte.setLongitud((float)longitud);
            reporte.setNombreConductor2(txt_conductor2.getText().toString());
            reporte.setMarca(txt_marca.getText().toString());
            reporte.setModelo(txt_modelo.getText().toString());
            reporte.setModelo(txt_color.getText().toString());
            reporte.setPlacasInvolucrado(txt_placas.getText().toString());
            reporte.setNombreAseguradora(txt_aseguradora.getText().toString());
            reporte.setNumPoliza(txt_poliza.getText().toString());
            task.execute(reporte);
        }

    }

    private boolean validar(){
        boolean ok = true;
        if(txt_marca.getText() == null || txt_marca.getText().toString().isEmpty()){
            txt_marca.setError("Debes introducir la marca del vehículo");
            ok = false;
        }
        if(txt_modelo.getText() == null || txt_modelo.getText().toString().isEmpty()){
            txt_modelo.setError("Debes introducir el modelo del vehículo");
            ok = false;
        }
        if(txt_color.getText() == null || txt_color.getText().toString().isEmpty()){
            txt_color.setError("Debes introducir el color del vehículo");
            ok = false;
        }
        if(txt_placas.getText() == null || txt_placas.getText().toString().isEmpty()){
            txt_placas.setError("Debes introducir las placas del vehículo");
        }
        return ok;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean online = (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected());
        if (!online) {
            Mensajes.mostrarAlertDialog("Sin conexion", "No se encontró ninguna conexión a Internet, conéctate " +
                    "a alguna red para continuar utilizando la aplicación",LevantarReporteInvolucrado.this);
        }
        return online;
    }

    //CLASE PARA GUARDAR EL REPORTE
    class WSPOSTRegistrarReporteTask extends AsyncTask<Object, String, String> {

        protected String doInBackground(Object ... params){
            resws = HttpUtils.registrarReporte((Reporte) params[0]);
            return null;
        }

        protected void onPostExecute(String result){
            super.onPostExecute(result);
            resultadoRegistrar();
        }
    }

    public void resultadoRegistrar(){
        if (resws != null && !resws.isError() && resws.getResult() != null) {
            subirFotos(1);
        }else{
            Mensajes.mostrarAlertDialog("Error", resws.getResult(),LevantarReporteInvolucrado.this);
        }
    }

    public void subirFotos(int folioReporte){
        espera = new ProgressDialog(this);
        espera.setTitle("Enviando el reporte");
        espera.setMessage("Espera por favor");
        espera.setCancelable(false);
        espera.show();
        for (int i = 0; i< numFotos; i++){
            String id = i+"";
            WSPOSTFotosTask task = new WSPOSTFotosTask();
            task.execute(folioReporte+id,fotos.get(i));
        }
        //finish();
    }

    class WSPOSTFotosTask extends AsyncTask<Object, String, String> {
        @Override
        protected String doInBackground(Object ... params) {
            resws = HttpUtils.subirFoto((String)params[0],(Bitmap)params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            numFotosSubidas++;
            if (numFotosSubidas == numFotos){
                resultadoFoto();
            }
        }
    }

    private void resultadoFoto() {
        espera.dismiss();
        finish();
        finish();
    }
}
