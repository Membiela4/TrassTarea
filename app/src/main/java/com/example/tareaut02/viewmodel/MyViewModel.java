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
    public String titulo,fechaInicio,fechaObjetivo, descripcion;
    public int progreso;
    public boolean prioritaria;

    public LiveData<List<Tarea>> getTareas() {
        if ( tareas == null) {
            tareas = new MutableLiveData<List<Tarea>>();

        }
        return tareas;
    }

}