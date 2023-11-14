package com.example.tareaut02.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tareaut02.model.Tarea;

import java.util.ArrayList;
import java.util.List;

public class MyViewModel extends ViewModel {
    private MutableLiveData<List<Tarea>> tareas;
    private ArrayList<Tarea> tareaList;
    private ArrayList<Tarea> tareasPreferentes;
    public LiveData<List<Tarea>> getTareas() {
        if ( tareas == null) {
            tareas = new MutableLiveData<List<Tarea>>();
            loadTasks();
        }
        return tareas;
    }

    private void loadTasks() {
        Tarea tarea1 = new Tarea("Planificación del Proyecto", "Crear un plan detallado para el proyecto de desarrollo de software.", 20, "10/10/2023", "27/10/2023", true);
        Tarea tarea2 = new Tarea("Revisión del Diseño", "Revisar y mejorar el diseño de la interfaz de usuario.", 50, "10/10/2023", "20/12/2023", false);
        Tarea tarea3 = new Tarea("Pruebas de Funcionalidad", "Realizar pruebas de funcionalidad en el módulo de usuario.", 10, "10/10/2023", "15/11/2023", false);
        Tarea tarea4 = new Tarea("Entrenamiento del Equipo", "Organizar una sesión de entrenamiento para el equipo de desarrollo.", 0, "10/10/2023", "20/11/2023", true);
        Tarea tarea5 = new Tarea("Informe de Progreso", "Preparar un informe de progreso mensual para la alta dirección.", 30, "10/10/2023", "20/10/23", true);

        tareaList.add(tarea1);
        tareaList.add(tarea2);
        tareaList.add(tarea3);
        tareaList.add(tarea4);
        tareaList.add(tarea5);
        tareasPreferentes.addAll(tareaList);
        tareas.setValue(tareaList);
    }
}