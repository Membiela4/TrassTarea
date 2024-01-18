package com.example.tareaut03.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

public class ConfiguracionAlmacenamiento {

    private static final String PREF_SD = "check_sd";
    private static final String PREF_LIMPIEZA = "numdias_limpieza";

    public static boolean getAlmacenamientoSD(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(PREF_SD, false);
    }

    public static void setAlmacenamientoSD(Context context, boolean valor) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(PREF_SD, valor).apply();
    }

    public static int getLimpiezaArchivos(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(PREF_LIMPIEZA, 0);
    }

    public static void setLimpiezaArchivos(Context context, int valor) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putInt(PREF_LIMPIEZA, valor).apply();
    }

    public static String obtenerDirectorioAlmacenamiento(Context context) {
        if (getAlmacenamientoSD(context) && isTarjetaSDMontada()) {
            return obtenerDirectorioSD();
        } else {
            return context.getFilesDir().getPath();
        }
    }

    private static boolean isTarjetaSDMontada() {
        String estado = Environment.getExternalStorageState();
        return estado.equals(Environment.MEDIA_MOUNTED);
    }

    private static String obtenerDirectorioSD() {
        return Environment.getExternalStorageDirectory().getPath() + "/archivosComunes";
    }
}
