package com.tequilacoders.transitox.utilerias;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.view.Window;

public class Mensajes {
    public static void mostrarAlertDialog(String titulo, String mensaje, Activity contexto) {
        AlertDialog dialog = new AlertDialog.Builder(contexto).create();
        dialog.setMessage(mensaje);
        dialog.setTitle(titulo);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    public static void mostrarAlertDialog(String titulo, String mensaje, Activity contexto, Drawable icono) {
        AlertDialog dialog = new AlertDialog.Builder(contexto).create();
        dialog.setMessage(mensaje);
        dialog.setTitle(titulo);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.setIcon(icono);
        dialog.show();
    }
}
