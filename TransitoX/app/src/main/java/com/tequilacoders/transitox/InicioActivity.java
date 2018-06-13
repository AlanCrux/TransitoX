package com.tequilacoders.transitox;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class InicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
    }

    public void mostrarIniciarSesion(View v) {
        Intent intent = new Intent(this, IniciarSesionActivity.class);
        startActivity(intent);
    }

    public void mostrarRegistrar(View v) {
        Intent intent = new Intent(this, RegistrarUsuario.class);
        startActivity(intent);
    }
}
