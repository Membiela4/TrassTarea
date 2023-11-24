package com.example.tareaut02.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.example.tareaut02.R;
import com.example.tareaut02.model.Tarea;
import com.example.tareaut02.viewmodel.MyViewModel;

public class SecondFragment extends Fragment {

    public onTaskCreatedListener comunicador2;
    TextView tvDescripcion;
    Button btnAdd,btnBack;
    MyViewModel sharedViewModel;
    FragmentTransaction fragmentTransaction;
    Tarea nuevaTarea;
    public SecondFragment() {
        // Required empty public constructor
    }
    public interface onTaskCreatedListener {
        void onTaskCreated();
        void onBack();
    }


    @Override
    public void onResume() {
        super.onResume();
        if(sharedViewModel !=null){
            if(sharedViewModel.getDescripcionTarea()!=null)
             tvDescripcion.setText(sharedViewModel.getDescripcionTarea().getValue());
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmento2 = inflater.inflate(R.layout.fragment_second,container,false);
        btnAdd = fragmento2.findViewById(R.id.btn_add);
        btnBack = fragmento2.findViewById(R.id.btn_back);
        tvDescripcion = fragmento2.findViewById(R.id.tv_descripcion);
        // Utilizando expresiones lambda para simplificar la asignación de OnClickListener
        btnAdd.setOnClickListener(v -> {
            sharedViewModel.setDescripcionTarea(tvDescripcion.getText().toString());
            comunicador2.onTaskCreated();

        });
        btnBack.setOnClickListener(v -> comunicador2.onBack());

        return fragmento2;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
         if (context instanceof onTaskCreatedListener )
            comunicador2 = (SecondFragment.onTaskCreatedListener) context;
        else
            throw new ClassCastException();
    }

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }


}