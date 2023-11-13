package com.example.tareaut02.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tareaut02.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTaskFirst#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTaskFirst extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    Button btnNext;
    private String tituloTarea;
    private String fechaInicio;
    private String fechaObjetivo;
    private boolean prioritaria;
    private int progreso;


    public AddTaskFirst() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AddTaskFirst newInstance() {
        AddTaskFirst fragment = new AddTaskFirst();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_task_first, container, false);
    }
}