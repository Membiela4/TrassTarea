package com.example.tareaut02.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

    private String tituloTarea;
    private String fechaInicio;
    private String fechaObjetivo;
    private boolean prioritaria;
    private int progreso;

    TextView txtTitulo,fechaInicioDate, fechaObjetivoDate;

    Spinner spinnerProgreso;
     CheckBox checkBoxPrioritaria;
    Button btnNext;







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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtTitulo = view.findViewById(R.id.tv_titulo);
        fechaInicioDate = view.findViewById(R.id.fecha_inicio_datepicker);
        fechaObjetivoDate = view.findViewById(R.id.fecha_objetivo_date_picker);
        checkBoxPrioritaria = view.findViewById(R.id.chechbox_prioritaria);
        btnNext = view.findViewById(R.id.button_next);
        btnNext.setOnClickListener(this::next);
        spinnerProgreso = view.findViewById(R.id.spinner_progreso);
        // Datos para el Spinner
        String[] opciones = {"0%", "25%", "50%", "75%", "100%"};
        // Crear el ArrayAdapter y configurarlo
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Asignar el adaptador al Spinner
        spinnerProgreso.setAdapter(adapter);
    }



    private void next(View view) {
        if (txtTitulo.getText() != null && fechaInicioDate.getText() != null && fechaObjetivoDate.getText() != null && String.valueOf(spinnerProgreso.getSelectedItem()) != null) {

            tituloTarea = txtTitulo.getText().toString();
            fechaInicio = fechaInicioDate.getText().toString();
            fechaObjetivo = fechaObjetivoDate.getText().toString();


            switch(spinnerProgreso.getSelectedItem().toString()) {
                case "0%":
                    progreso = 0;
                    break;
                case "25%":
                    progreso = 25;
                    break;
                case "50%":
                    progreso = 50;
                    break;
                case "75%":
                    progreso = 75;
                    break;
                case "100%":
                    progreso = 100;
                    break;
            }
            if (checkBoxPrioritaria.isChecked())
                prioritaria = true;
            else
                prioritaria = false;
            Bundle bundle = new Bundle();
            bundle.putString("tituloTarea", tituloTarea);
            bundle.putString("fechaInicio", fechaInicio);
            bundle.putString("fechaObj", fechaObjetivo);
            bundle.putInt("progreso", progreso);
            bundle.putBoolean("prioritaria", prioritaria);
            getParentFragmentManager().setFragmentResult("tarea", bundle);

            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            SecondFragment fragmentSecond = new SecondFragment();

            // Pasa los datos al segundo fragmento
            fragmentSecond.setArguments(bundle);

            // Reemplaza el fragmento actual con el fragmento_second
            fragmentTransaction.replace(R.id.fragmentContainerView, fragmentSecond);
            fragmentTransaction.commit();

        } else {
            Toast toast = new Toast(this.getContext());
            toast.setText("INTRODUCE TODOS LOS VALORES");
            toast.show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_add_task_first, container, false);
    }
}