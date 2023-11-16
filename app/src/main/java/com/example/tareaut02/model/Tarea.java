package com.example.tareaut02.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tarea implements Parcelable {
    private String titulo;
    private String descripcion;
    private int progreso;
    private String fechaInicio;
    private String fechaFinal;
    private boolean prioritaria;

    protected Tarea(Parcel in) {
        titulo = in.readString();
        descripcion = in.readString();
        progreso = in.readInt();
        fechaInicio = in.readString();
        fechaFinal = in.readString();
        prioritaria = in.readByte() != 0;
    }

    public static final Creator<Tarea> CREATOR = new Creator<Tarea>() {
        @Override
        public Tarea createFromParcel(Parcel in) {
            return new Tarea(in);
        }

        @Override
        public Tarea[] newArray(int size) {
            return new Tarea[size];
        }
    };

    public Tarea(MutableLiveData<String> tituloTarea, MutableLiveData<String> descripcionTarea, MutableLiveData<Integer> progreso, MutableLiveData<String> fechaInicio, MutableLiveData<String> fechaFinalizacion, MutableLiveData<Boolean> tareaPrioritaria) {
        this.titulo = tituloTarea.getValue();
        this.descripcion = descripcionTarea.getValue();
        this.progreso = progreso.getValue();
        this.fechaInicio = fechaInicio.getValue();
        this.fechaFinal = fechaFinalizacion.getValue();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getProgreso() {
        return progreso;
    }

    @SuppressLint("SuspiciousIndentation")
    public void setProgreso(int progreso) {
        if(progreso>0 && progreso<101)
        this.progreso = progreso;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public boolean isPrioritaria() {
        return prioritaria;
    }

    public void setPrioritaria(boolean prioritaria) {
        this.prioritaria = prioritaria;
    }

    public Tarea(String titulo, String descripcion, int progreso, String fechaInicio, String fechaFinal, boolean prioritaria) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.progreso = progreso;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.prioritaria = prioritaria;
    }

    public Tarea(){

    }

    public int diasRestantes(String fechaObjetivo) {
        // Formato de fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            // Obtiene la fecha actual
            Date fechaActual = new Date();
            // Convierte la fecha objetivo de String a Date
            Date fechaObjetivoDate = sdf.parse(fechaObjetivo);

            // Calcula la diferencia en milisegundos
            long diferenciaEnMilisegundos = fechaObjetivoDate.getTime() - fechaActual.getTime();

            // Convierte la diferencia de milisegundos a días
            int diasRestantes = (int) (diferenciaEnMilisegundos / (1000 * 60 * 60 * 24));

            return diasRestantes;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeString(descripcion);
        dest.writeInt(progreso);
        dest.writeString(fechaInicio);
        dest.writeString(fechaFinal);
        dest.writeByte((byte) (prioritaria ? 1 : 0));
    }
}
