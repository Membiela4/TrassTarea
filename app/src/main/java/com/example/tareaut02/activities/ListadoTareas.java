package com.example.tareaut02.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.tareaut02.R;
import com.example.tareaut02.adapters.TareaAdapter;
import com.example.tareaut02.model.Tarea;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListadoTareas extends AppCompatActivity  {

    private RecyclerView listaTareas;
    private TareaAdapter tareaAdapter;
    private List<Tarea> tareaList;
    private List<Tarea> tareasPreferentes;
    private boolean mostrarPreferentes = false;
    private Tarea tareaSeleccionada;
    private int selectedTaskPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado_tareas);
        listaTareas = findViewById(R.id.listaTareas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listaTareas.setLayoutManager(layoutManager);

        tareaList = new ArrayList<>();
        tareasPreferentes = new ArrayList<>();

        tareaAdapter = new TareaAdapter(this, tareaList);
        listaTareas.setAdapter(tareaAdapter);

        chargeList();
        updateUI();


        class MiViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            public MiViewHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnCreateContextMenuListener(this);

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        selectedTaskPosition = getAdapterPosition();
                        tareaSeleccionada = tareaList.get(selectedTaskPosition);
                        return false;
                    }
                });
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_contextual, menu);
                tareaSeleccionada = tareaList.get(selectedTaskPosition);

            }
        }


        registerForContextMenu(listaTareas);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);

        // Cambia el ícono según el estado de mostrarPreferentes
        MenuItem prioritariasItem = menu.findItem(R.id.prioritarias);
        if (mostrarPreferentes) {
            prioritariasItem.setIcon(R.drawable.btn_star__on);
        } else {
            prioritariasItem.setIcon(R.drawable.btn_star_off);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuCrearTarea) {
            // Abre la actividad de creación de tarea
            Intent intent = new Intent(this, CrearTareaActivity.class);
            launcher.launch(intent);
            return true;
        } else if (id == R.id.menuAcercaDe) {
            mostrarVentanaEmergente();
            return true;
        } else if (id == R.id.menuSalir) {
            // Realiza la acción de salida o cierre de la aplicación
            finishAffinity();
            return true;
        } else if (id == R.id.prioritarias) {
            mostrarPreferentes = !mostrarPreferentes; // Cambia el estado
            updateUI(); // Actualiza la vista
            tareaAdapter.notifyDataSetChanged();

            // Llama a la función que actualiza el menú para cambiar el ícono
            invalidateOptionsMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void mostrarVentanaEmergente() {
        // Obtener el inflador de diseño
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.ventana_emergente_acercade, null);

        // Configurar el contenido de la ventana emergente
        TextView contenidoVentanaEmergente = view.findViewById(R.id.contenido_ventana_emergente);
        contenidoVentanaEmergente.setText("Título de la aplicación: TrassTarea\n" +
                "Nombre del centro: IES Trassierra\n" +
                "Autor: David Membiela\n" +
                "Año actual: 2023");

        // Crear el cuadro de diálogo emergente
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Código a ejecutar cuando se hace clic en Aceptar
                        dialog.dismiss(); // Cierra la ventana emergente
                    }
                });

        // Mostrar el cuadro de diálogo
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void updateUI() {
        if (mostrarPreferentes) {

            tareaAdapter.setTareas(tareasPreferentes);

        } else {
            tareaAdapter.setTareas(tareaList);
        }
        tareaAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        Tarea tarea = tareaAdapter.getTarea();
        if (itemId == R.id.descripcion_menu_item) {
            showDescriptionDialog(tarea.getDescripcion());
        } else if (itemId == R.id.borrar_menu_item) {
            mostrarDialogoConfirmacionBorrado(tarea);
        }else if(itemId == R.id.editar_menu_item){
            Intent intent = new Intent(this, EditarTareaActivity.class);
            //pasar la tarea a la actividad de editar
            launcher.launch(intent);
        }

        return super.onContextItemSelected(item);
    }
    private void mostrarDialogoConfirmacionBorrado(final Tarea tarea) {
        // Crear un AlertDialog para confirmar la eliminación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar eliminación");
        builder.setMessage("¿Estás seguro de que quieres eliminar esta tarea? ("+tarea.getTitulo()+")");

        // Agregar botón de cancelar
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // El usuario ha cancelado la eliminación, no hacer nada
                dialog.dismiss();
            }
        });

        // Agregar botón de confirmación
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Eliminar la tarea seleccionada de la lista y actualizar la UI
                tareaList.remove(tarea);
                if (tarea.isPrioritaria()) {
                    tareasPreferentes.remove(tarea);
                }

                // Actualizar la interfaz de usuario
                updateUI();

                // Mostrar un Snackbar indicando que la tarea ha sido eliminada
                showSnackbar("Tarea eliminada");
            }
        });

        // Mostrar el diálogo
        builder.show();
    }



    private void showDescriptionDialog(String descripcion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Descripción")
                .setMessage(descripcion)
                .setPositiveButton("Aceptar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showSnackbar(String message) {
        Snackbar.make(listaTareas, message, Snackbar.LENGTH_SHORT).show();
    }


    private void chargeList() {
        Tarea tarea1 = new Tarea("Planificación del Proyecto", "Crear un plan detallado para el proyecto de desarrollo de software.", 20, "10/10/2023", "27/10/2023", true);
        Tarea tarea2 = new Tarea("Revisión del Diseño", "Revisar y mejorar el diseño de la interfaz de usuario.", 50, "10/10/2023", "20/12/2023", false);
        Tarea tarea3 = new Tarea("Pruebas de Funcionalidad", "Realizar pruebas de funcionalidad en el módulo de usuario.", 100, "10/10/2023", "15/11/2023", false);
        Tarea tarea4 = new Tarea("Entrenamiento del Equipo", "Organizar una sesión de entrenamiento para el equipo de desarrollo.", 0, "10/10/2023", "20/11/2023", true);
        Tarea tarea5 = new Tarea("Informe de Progreso", "Preparar un informe de progreso mensual para la alta dirección.", 30, "10/10/2023", "20/10/2023", true);

        tareaList.add(tarea1);
        tareaList.add(tarea2);
        tareaList.add(tarea3);
        tareaList.add(tarea4);
        tareaList.add(tarea5);
        for (Tarea t:tareaList) {
            if(t.isPrioritaria())
                tareasPreferentes.add(t);
        }
    }

    ActivityResultContract<Intent, ActivityResult> contract = new ActivityResultContracts.StartActivityForResult();

    ActivityResultCallback<ActivityResult> respuesta = new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK){
                Intent intent = result.getData();
                Tarea tarea = (Tarea) intent.getSerializableExtra("tarea");
                tareaList.add(tarea);
                updateUI();
                tareaAdapter.notifyDataSetChanged();

            }
        }
    };

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(contract, respuesta);
}
