package com.tequilacoders.transitox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ListaVehiculos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_vehiculos);
    }

    public void onActionRegistrarAutomovil(View v){
        Intent intent = new Intent(this, RegistrarVehiculo.class);
        startActivity(intent);
    }
}
