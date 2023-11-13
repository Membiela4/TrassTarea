package com.example.tareaut02.model;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tarea {
    private String titulo;
    private String descripcion;
    private int progreso;
    private String fechaInicio;
    private String fechaFinal;
    private boolean prioritaria;

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
        if(this.progreso>0 && this.progreso<101)
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

    @Override
    public String toString() {
        return "Tarea{" +
                "titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", progreso=" + progreso +
                ", fechaInicio=" + fechaInicio +
                ", fechaFinal=" + fechaFinal +
                ", prioritaria=" + prioritaria +
                '}';
    }

    public int diasRestantes(String fechaObjetivo) {
        // Formato de fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            // Obtiene la fecha actual
            Date fechaActual = new Date();
            fechaObjetivo = this.getFechaFinal();
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
}
