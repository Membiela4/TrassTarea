package com.example.tareaut02.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

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
import com.example.tareaut02.viewmodel.MyViewModel;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {

    private String tituloTarea;
    private String fechaInicio;
    private String fechaObjetivo;
    private boolean prioritaria;
    private int progreso;
    MyViewModel viewModel;
    TextView txtTitulo,fechaInicioDate, fechaObjetivoDate;
    Spinner spinnerProgreso;
     CheckBox checkBoxPrioritaria;
    Button btnNext;
    Button btnBack;

    public FirstFragment() {
        // Required empty public constructor
    }

    public interface OnNextBtnClicked{
       public void btnSiguiente();
       public void btnCancelar();
       
    }

    public void setValores(){
        txtTitulo.setText(viewModel.getTituloTarea().getValue());
        fechaInicioDate.setText(viewModel.getFechaInicio().getValue());
        fechaObjetivoDate.setText(viewModel.getFechaFinalizacion().getValue());
        if(viewModel.getProgreso().getValue()!=null){
            switch (viewModel.getProgreso().getValue()) {
                case 0:
                    spinnerProgreso.setSelection(0);
                    break;
                case 25:
                    spinnerProgreso.setSelection(1);
                    break;
                case 50:
                    spinnerProgreso.setSelection(2);
                    break;
                case 75:
                    spinnerProgreso.setSelection(3);
                    break;
                case 100:
                    spinnerProgreso.setSelection(4);
                    break;
                // Puedes manejar otros valores o casos según sea necesario
                default:
                    spinnerProgreso.setSelection(0);
            }
        }


        if (viewModel.getTareaPrioritaria().getValue() != null && viewModel.getTareaPrioritaria().getValue()) {
            checkBoxPrioritaria.setChecked(true);
        } else {
            checkBoxPrioritaria.setChecked(false);
        }
    }

    public OnNextBtnClicked comunicador1;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnNextBtnClicked)
            comunicador1 = (OnNextBtnClicked) context;
        else
            throw new ClassCastException();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);

    }

    private void next(View view) {
        if (txtTitulo.getText() != null && fechaInicioDate.getText() != null && fechaObjetivoDate.getText() != null && String.valueOf(spinnerProgreso.getSelectedItem()) != null) {

            tituloTarea = txtTitulo.getText().toString();
            fechaInicio = fechaInicioDate.getText().toString();
            fechaObjetivo = fechaObjetivoDate.getText().toString();

            switch(spinnerProgreso.getSelectedItemPosition()) {
                case 0:
                    progreso = 0;
                    break;
                case 1:
                    progreso = 25;
                    break;
                case 2:
                    progreso = 50;
                    break;
                case 3:
                    progreso = 75;
                    break;
                case 4:
                    progreso = 100;
                    break;
            }
            if (checkBoxPrioritaria.isChecked())
                prioritaria = true;
            else
                prioritaria = false;


            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            SecondFragment fragmentSecond = new SecondFragment();


            viewModel.setTituloTarea(tituloTarea);
            viewModel.setFechaInicio(fechaInicio);
            viewModel.setFechaFinalizacion(fechaObjetivo);
            viewModel.setProgreso(progreso);
            viewModel.setPrioritaria(prioritaria);


            // Reemplaza el fragmento actual con el fragmento_second
            fragmentTransaction.replace(R.id.fragment_container_editar, fragmentSecond);
            fragmentTransaction.commit();

        } else {
            Toast toast = new Toast(this.getContext());
            toast.setText("INTRODUCE TODOS LOS VALORES");
            toast.show();
        }
    }


    public void mostrarDatePicker(View view) {
        final Calendar calendario = Calendar.getInstance();
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        int anioFin = calendario.get(Calendar.YEAR);
        int mesFin = calendario.get(Calendar.MONTH);
        int diaFin = calendario.get(Calendar.DAY_OF_MONTH);

        // Utiliza el estilo de fecha adecuado para el DatePickerDialog
        DatePickerDialog datePickerDialogInicio = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Actualiza el EditText de fecha de inicio con la fecha seleccionada por el usuario
                fechaInicioDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        }, anio, mes, dia);

        DatePickerDialog datePickerDialogFin = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Actualiza el EditText de fecha final con la fecha seleccionada por el usuario
                fechaObjetivoDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        }, anioFin, mesFin, diaFin);

        // Muestra el DatePickerDialog correspondiente según el botón clicado
        if (view.getId() == R.id.fecha_inicio_datepicker) {
            datePickerDialogInicio.show();
        } else if (view.getId() == R.id.fecha_objetivo_date_picker) {
            datePickerDialogFin.show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task_first,container,false);
        super.onViewCreated(view, savedInstanceState);
        txtTitulo = view.findViewById(R.id.tv_titulo);
        fechaInicioDate = view.findViewById(R.id.fecha_inicio_datepicker);
        fechaObjetivoDate = view.findViewById(R.id.fecha_objetivo_date_picker);
        fechaInicioDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePicker(fechaInicioDate);
            }
        });

        fechaObjetivoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePicker(fechaObjetivoDate);
            }
        });
        checkBoxPrioritaria = view.findViewById(R.id.chechbox_prioritaria);
        btnNext = view.findViewById(R.id.button_next);
        btnBack = view.findViewById(R.id.back_btn);


        spinnerProgreso = view.findViewById(R.id.spinner_progreso);
        // Datos para el Spinner
        String[] opciones = {"0%", "25%", "50%", "75%", "100%"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Asignar el adaptador al Spinner
        spinnerProgreso.setAdapter(adapter);
        btnNext.setOnClickListener(v -> {
            next(view);
            comunicador1.btnSiguiente();
        });
        btnBack.setOnClickListener(v -> {
            comunicador1.btnCancelar();
        });

        setValores();



        return view;
    }


}