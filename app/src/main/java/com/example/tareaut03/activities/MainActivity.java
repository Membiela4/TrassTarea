package com.example.tareaut03.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tareaut03.R;

public class MainActivity extends AppCompatActivity {


    Button startBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startBtn = findViewById(R.id.startBtn);

    }

    public void changeView(View v){
            Intent intent = new Intent(getApplicationContext(), ListadoTareas.class);
            startActivity(intent);

    }

    @Override
    public Resources.Theme getTheme() {
        SharedPreferences sharedPreferences  = PreferenceManager.getDefaultSharedPreferences(this);
        boolean valorTema = sharedPreferences.getBoolean("switch_tema", false);
        if(valorTema){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        return super.getTheme();

    }
}