package com.example.tareaut03.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tareaut03.bbdd.BaseDatosApp;
import com.example.tareaut03.model.Tarea;

import java.util.ArrayList;
import java.util.List;

public class MyViewModel extends ViewModel {

    LiveData<List<Tarea>> tareas;

    private MutableLiveData<List<Tarea>> preferentes = new MutableLiveData<>();

    private final MutableLiveData<String> titulo = new MutableLiveData<>();
    private final MutableLiveData<Boolean> prioritaria = new MutableLiveData<>();
    private final MutableLiveData<String> descripcion = new MutableLiveData<>();
    private final MutableLiveData<String> fechaInicio = new MutableLiveData<>();
    private final MutableLiveData<String> fechaObjetivo = new MutableLiveData<>();
    private final MutableLiveData<Integer> progreso = new MutableLiveData<>();
    private final MutableLiveData<String> documentURL = new MutableLiveData<>();
    private final MutableLiveData<String> imageURL = new MutableLiveData<>();
    private final MutableLiveData<String> audioURL = new MutableLiveData<>();
    private final MutableLiveData<String> videoURL = new MutableLiveData<>();

    private final MutableLiveData<Integer> posicion = new MutableLiveData<>();

        private MutableLiveData<Tarea> tareaEditada = new MutableLiveData<>();

    public MyViewModel(@NonNull Application application) {

        tareas = BaseDatosApp.getInstance(application).tareadao().getAll();
        if(tareas==null){
            tareas = new MutableLiveData<List<Tarea>>() {
            };
        }
    }



    public LiveData<List<Tarea>> getTareas() {
        return tareas;
    }

    public LiveData<List<Tarea>> getPreferentes() {
        List<Tarea> todasLasTareas = getTareas().getValue();

        List<Tarea> preferentesList = new ArrayList<>();
        for (Tarea tarea : todasLasTareas) {
            if (tarea.isPrioritaria()) {
                preferentesList.add(tarea);
            }
        }

        preferentes.setValue(preferentesList);
        return preferentes;
    }

    public MyViewModel(){

    }

    public void setTareaEditada(Tarea tarea) {
            tareaEditada.setValue(tarea);
        }

        public LiveData<Tarea> getTareaEditada() {
            return tareaEditada;
        }

    public void setPosicion(Integer posicion){this.posicion.setValue(posicion);}
    public void setTituloTarea(String tituloTarea)
    {
        this.titulo.setValue(tituloTarea);
    }
    public void setPrioritaria(Boolean prioritaria) {
        this.prioritaria.setValue(prioritaria);
    }

    public void setDescripcionTarea(String descripcionTarea)
    {
        this.descripcion.setValue(descripcionTarea);
    }public void setFechaInicio(String fechaInicio)
    {
        this.fechaInicio.setValue(fechaInicio);
    }public void setFechaFinalizacion(String fechaFinalizacion)
    {
        this.fechaObjetivo.setValue(fechaFinalizacion);
    }

    public void setProgreso(Integer progreso)
    {
        this.progreso.setValue(progreso);
    }


    public void setDocumentURL(String url) {
        this.documentURL.setValue(url);
    }

    public void setImageURL(String url){
            this.imageURL.setValue(url);
    }


    public MutableLiveData<String> getTituloTarea() {
        return titulo;
    }
    public MutableLiveData<String> getDescripcionTarea() {return descripcion;}
    public MutableLiveData<Boolean> getTareaPrioritaria() {return prioritaria;}
    public MutableLiveData<String> getFechaInicio() {return fechaInicio;}
    public MutableLiveData<String> getFechaFinalizacion() {return fechaObjetivo;}
    public MutableLiveData<Integer> getProgreso() {return progreso;}
    public MutableLiveData<Integer> getPosicion(){return posicion;}

    public MutableLiveData<String> getDocumentURL() {return documentURL;}

    public MutableLiveData<String> getImageURL() {return imageURL;}

    public MutableLiveData<String> getAudioURL() {return audioURL;}
    public MutableLiveData<String> getVideoURL() {return videoURL;}


    public void setTareas(List<Tarea> todasLasTareas) {
        tareas = (LiveData<List<Tarea>>) todasLasTareas;
    }
}