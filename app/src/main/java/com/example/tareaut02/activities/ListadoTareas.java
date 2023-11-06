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
        updateUI();
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
}