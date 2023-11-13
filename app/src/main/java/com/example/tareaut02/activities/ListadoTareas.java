package com.example.tareaut02.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tareaut02.R;
import com.example.tareaut02.adapters.TareaAdapter;
import com.example.tareaut02.model.Tarea;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class ListadoTareas extends AppCompatActivity {

    private RecyclerView listaTareas;
    private TareaAdapter tareaAdapter;
    private List<Tarea> tareaList;
    private List<Tarea> tareasPreferentes;
    private boolean mostrarPreferentes = false;
    private Tarea tareaSeleccionada;

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

        // Set up the context menu listener for each item in the RecyclerView
        class MiViewHolder extends RecyclerView.ViewHolder{
            public MiViewHolder(@NonNull View itemView) {
                super(itemView);

                itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        MenuInflater inflater = getMenuInflater();
                        inflater.inflate(R.menu.menu_contextual,menu);
                        tareaSeleccionada = tareaList.get(getAdapterPosition());
                    }
                });
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
            startActivity(intent);
            return true;
        } else if (id == R.id.menuAcercaDe) {
            // Abre ventana emergente de texto con datos informativos de la aplicación
            // (Puedes modificar esto para mostrar una actividad de Acerca de si lo prefieres)
            // Toast.makeText(this, "Acerca de", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menuSalir) {
            // Realiza la acción de salida o cierre de la aplicación
            finish();
            return true;
        } else if (id == R.id.prioritarias) {
            mostrarPreferentes = !mostrarPreferentes; // Cambia el estado
            updateUI(); // Actualiza la vista

            // Llama a la función que actualiza el menú para cambiar el ícono
            invalidateOptionsMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        if (itemId == R.id.descripcion_menu_item) {
            DialogFragment dialogFragment = new DialogFragment();
            dialogFragment.setCancelable(true);
            String descripcion = tareaSeleccionada.getDescripcion();
            Toast toast = new Toast(this);
            toast.setText(descripcion);
            toast.show();
        } else if (itemId == R.id.borrar_menu_item) {
           listaTareas.removeItemDecorationAt(itemId);
        }
        return super.onContextItemSelected(item);
    }

    private void chargeList() {
        Tarea tarea1 = new Tarea("Planificación del Proyecto", "Crear un plan detallado para el proyecto de desarrollo de software.", 20, "10/10/2023", "27/10/2023", true);
        Tarea tarea2 = new Tarea("Revisión del Diseño", "Revisar y mejorar el diseño de la interfaz de usuario.", 50, "10/10/2023", "20/12/2023", false);
        Tarea tarea3 = new Tarea("Pruebas de Funcionalidad", "Realizar pruebas de funcionalidad en el módulo de usuario.", 10, "10/10/2023", "15/11/2023", false);
        Tarea tarea4 = new Tarea("Entrenamiento del Equipo", "Organizar una sesión de entrenamiento para el equipo de desarrollo.", 0, "10/10/2023", "20/11/2023", true);
        Tarea tarea5 = new Tarea("Informe de Progreso", "Preparar un informe de progreso mensual para la alta dirección.", 30, "10/10/2023", "20/10/23", true);

        tareaList.add(tarea1);
        tareaList.add(tarea2);
        tareaList.add(tarea3);
        tareaList.add(tarea4);
        tareaList.add(tarea5);
        tareasPreferentes.addAll(tareaList);
    }
}
