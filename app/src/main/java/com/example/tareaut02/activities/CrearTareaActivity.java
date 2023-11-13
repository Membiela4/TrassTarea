package com.example.tareaut02.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tareaut02.R;
import com.example.tareaut02.model.Tarea;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CrearTareaActivity extends AppCompatActivity {


    Button btnBack;
    private Tarea tareaSeleccionada;
    private ArrayList<Tarea> tareaList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_tarea);
        btnBack = findViewById(R.id.backBtn);
        btnBack.setOnClickListener(this::back);

    }

    private void back(View view){
        finish();
    }
}