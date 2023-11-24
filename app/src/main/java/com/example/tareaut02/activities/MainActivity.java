package com.example.tareaut02.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.Lottie;
import com.example.tareaut02.R;

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

}