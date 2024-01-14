package com.example.tareaut02.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class Tarea implements Parcelable, Serializable {
    private String titulo;
    private String descripcion;
    private int progreso;
    private String fechaInicio;
    private String fechaFinal;
    private boolean prioritaria;
    private String document_url;
    private String image_url;
    private String audio_url;
    private String video_url;

    public Tarea(MutableLiveData<String> tituloTarea, MutableLiveData<String> descripcionTarea, MutableLiveData<Integer> progreso, MutableLiveData<String> fechaInicio, MutableLiveData<String> fechaFinalizacion, MutableLiveData<Boolean> tareaPrioritaria, MutableLiveData<String> imageURL, MutableLiveData<String> documentURL) {
        this.titulo = tituloTarea.getValue();
        this.descripcion = descripcionTarea.getValue();
        this.progreso = progreso.getValue();
        this.fechaInicio = fechaInicio.getValue();
        this.fechaFinal = fechaFinalizacion.getValue();
        this.prioritaria = tareaPrioritaria.getValue();
        this.image_url = imageURL.getValue();
        this.document_url = documentURL.getValue();
    }

    public String getDocument_url() {
        return document_url;
    }

    public void setDocument_url(String document_url) {
        this.document_url = document_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }



    public Tarea(MutableLiveData<String> tituloTarea, MutableLiveData<String> descripcionTarea, MutableLiveData<Integer> progreso, MutableLiveData<String> fechaInicio, MutableLiveData<String> fechaFinalizacion, MutableLiveData<Boolean> tareaPrioritaria) {
        this.titulo = tituloTarea.getValue();
        this.descripcion = descripcionTarea.getValue();
        this.progreso = progreso.getValue();
        this.fechaInicio = fechaInicio.getValue();
        this.fechaFinal = fechaFinalizacion.getValue();
        this.prioritaria = tareaPrioritaria.getValue();
    }

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
        if(progreso>=0 && progreso<101)
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

            Calendar calendarActual = new GregorianCalendar();
            Date fechaActual = calendarActual.getTime();
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
