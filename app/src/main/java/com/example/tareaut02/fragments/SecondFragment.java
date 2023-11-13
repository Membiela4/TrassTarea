package com.example.tareaut02.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tareaut02.R;
import com.example.tareaut02.model.Tarea;

import java.util.ArrayList;


public class SecondFragment extends Fragment {

    TextView tvDescripcion;
    Button btnAdd;
    ArrayList<Tarea> tareas;
    String titulo , fechaInicio ,fechaObjetivo;
    int progreso ;
    boolean prioritaria ;
    public SecondFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("tarea", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                  titulo = result.getString("tituloTarea");
                  fechaInicio =result.getString("fechaInicio");
                  fechaObjetivo =result.getString("fechaObj");
                  progreso = result.getInt("progreso");
                  prioritaria = result.getBoolean("prioritaria");
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnAdd = view.findViewById(R.id.btn_add);
        tvDescripcion = view.findViewById(R.id.tv_descripcion);
        btnAdd.setOnClickListener(this::createTarea);
    }

    private void createTarea(View view){
        if(!(tvDescripcion.getText() ==null)){
            Tarea t = new Tarea();
            t.setTitulo(titulo);
            t.setFechaInicio(fechaInicio);
            t.setFechaFinal(fechaObjetivo);
            t.setProgreso(progreso);
            t.setPrioritaria(prioritaria);
            t.setDescripcion(tvDescripcion.getText().toString());
            tareas.add(t);
        }else{
            Toast toast = new Toast(this.getContext());
            toast.setText("INTRODUCE UNA DESCRIPCION");
            toast.show();
        }

    }
}