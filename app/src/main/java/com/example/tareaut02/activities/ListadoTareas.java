package com.example.tareaut02.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tareaut02.R;
import com.example.tareaut02.adapters.TareaAdapter;
import com.example.tareaut02.model.Tarea;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListadoTareas extends AppCompatActivity {

    private RecyclerView listaTareas;
    private TextView noTask;
    private TareaAdapter tareaAdapter;
    private List<Tarea> tasks = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado_tareas);
        noTask = findViewById(R.id.textNoTask);
        listaTareas = findViewById(R.id.listaTareas);
        layoutManager = new LinearLayoutManager(this);
        listaTareas.setLayoutManager(layoutManager);
        tareaAdapter = new TareaAdapter(this,tasks);
        listaTareas.setAdapter(tareaAdapter);
        chargeList();
        updateUI();
        //tareaAdapter.setTareas()
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void updateUI() {
        if (tasks.isEmpty()) {
            listaTareas.setVisibility(View.GONE);
            noTask.setVisibility(View.VISIBLE);
        } else {
            listaTareas.setVisibility(View.VISIBLE);
            noTask.setVisibility(View.GONE);
        }
    }
    //funcion que cambia el icono y las tareas preferentes
    private void setPrefers(){

    }

    private void chargeList() {
        Tarea tarea1 = new Tarea("Planificación del Proyecto", "Crear un plan detallado para el proyecto de desarrollo de software.", 20, "10/10/2023","", true);
        Tarea tarea2 = new Tarea("Revisión del Diseño", "Revisar y mejorar el diseño de la interfaz de usuario.", 50, "10/10/2023" , "20/12/2023", false);
        Tarea tarea3 = new Tarea("Pruebas de Funcionalidad", "Realizar pruebas de funcionalidad en el módulo de usuario.", 10,"10/10/2023","", false);
        Tarea tarea4 = new Tarea("Entrenamiento del Equipo", "Organizar una sesión de entrenamiento para el equipo de desarrollo.", 0, "10/10/2023", "20/11/2023", true);
        Tarea tarea5 = new Tarea("Informe de Progreso", "Preparar un informe de progreso mensual para la alta dirección.", 30, "10/10/2023","20/10/23" , true);

        tasks.add(tarea1);
        tasks.add(tarea2);
        tasks.add(tarea3);
        tasks.add(tarea4);
        tasks.add(tarea5);


    }
}