package com.tequilacoders.transitox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
        Intent intent = new Intent(this, IniciarSesionActivity.class);
        startActivity(intent);
    }
}
