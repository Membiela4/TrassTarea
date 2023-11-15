package com.example.tareaut02.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tareaut02.R;
import com.example.tareaut02.model.Tarea;

import java.util.ArrayList;


public class SecondFragment extends Fragment {

    TextView tvDescripcion;
    Button btnAdd,btnBack;
    ArrayList<Tarea> tareas;
    String titulo , fechaInicio ,fechaObjetivo;
    int progreso ;
    boolean prioritaria ;
    FragmentTransaction fragmentTransaction;
    public SecondFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getParentFragmentManager().setFragmentResultListener("tarea", this, (requestKey, result) -> {
              titulo = result.getString("tituloTarea");
              fechaInicio =result.getString("fechaInicio");
              fechaObjetivo =result.getString("fechaObj");
              progreso = result.getInt("progreso");
              prioritaria = result.getBoolean("prioritaria");
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
        btnBack = view.findViewById(R.id.btn_back);
        tvDescripcion = view.findViewById(R.id.tv_descripcion);
        btnAdd.setOnClickListener(this::createTarea);
        btnBack.setOnClickListener(this::back);

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

    private void back(View view){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AddTaskFirst firstFragment = new AddTaskFirst();

        // Reemplaza el fragmento actual con el fragmento_second
        fragmentTransaction.replace(R.id.fragmentContainerView, firstFragment);
        fragmentTransaction.commit();

    }
}