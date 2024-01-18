package com.example.tareaut03.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.tareaut03.R;
import com.example.tareaut03.adapters.TareaAdapter;
import com.example.tareaut03.bbdd.BaseDatosApp;
import com.example.tareaut03.model.Tarea;
import com.example.tareaut03.viewmodel.MyViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ListadoTareas extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView listaTareas;
    public TareaAdapter tareaAdapter;
    private List<Tarea> tareaList;
    private List<Tarea> tareasPreferentes;
    private boolean mostrarPreferentes = false;
    private Tarea tareaSeleccionada;
    private int indiceTarea;
    private int selectedTaskPosition = 0;
    private MyViewModel viewModel;

    SharedPreferences preferencias;

    static BaseDatosApp baseDatosApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado_tareas);
        viewModel = new MyViewModel(this.getApplication());

        baseDatosApp = BaseDatosApp.getInstance(this);


        listaTareas = findViewById(R.id.listaTareas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listaTareas.setLayoutManager(layoutManager);

        tareaList = new ArrayList<>();
        tareasPreferentes = new ArrayList<>();

        // Initialize the adapter first
        tareaAdapter = new TareaAdapter(this, tareaList);
        listaTareas.setAdapter(tareaAdapter);

        // Now observe changes in the LiveData
        viewModel.getTareas().observe(this, tareaAdapter::setDatos);

        preferencias = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        comprobarOrdenInicio();
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



    public void cambiarOrdenListas(){
        Collections.reverse(tareaList);
        if (mostrarPreferentes) {
            viewModel.getPreferentes().observe(this, tareas -> {
                tareaAdapter.setDatos(tareaList);
            });
        } else {
            viewModel.getTareas().observe(this, tareas -> {
                tareaAdapter.setDatos(tareaList);
            });
        }

        tareaAdapter.notifyDataSetChanged();
    }



    public void ordenarListas() {
        SharedPreferences a = PreferenceManager.getDefaultSharedPreferences(this);
        String criterio = a.getString("criterio_key", "orden_value");

        switch (criterio) {
            case "orden_value":
                tareaAdapter.setDatos( tareaList = baseDatosApp.tareadao().getTareasbyOrdenAlfabetico());
                break;
            case "fecha_value":
                tareaAdapter.setDatos( tareaList = baseDatosApp.tareadao().getTareasOrderByFecha().getValue());
                break;
            case "dias_value":
                tareaAdapter.setDatos( tareaList = baseDatosApp.tareadao().getTareasbyProgreso());
                break;
            case "prioritaria_value":
                tareaAdapter.setDatos( tareaList = baseDatosApp.tareadao().getTareasOrderByPrioritarias());
                break;
        }

        if (mostrarPreferentes) {
            viewModel.getPreferentes().observe(this, tareas -> {
                tareaAdapter.setDatos(tareaList);
            });
        } else {
            viewModel.getTareas().observe(this, tareas -> {
                tareaAdapter.setDatos(tareaList);
            });
        }
        viewModel.getTareas().observe(this, tareaAdapter::setDatos);

        tareaAdapter.notifyDataSetChanged();
    }


    public void comprobarOrdenInicio(){
        SharedPreferences a = PreferenceManager.getDefaultSharedPreferences(this);
        boolean ordenacion = a.getBoolean("switch_orden_key", true);
        if (ordenacion){
            cambiarOrdenListas();
        }
    }


    ActivityResultContract<Intent, ActivityResult> contract = new ActivityResultContracts.StartActivityForResult();

    ActivityResultCallback<ActivityResult> respuesta = new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK){
                Intent intent = result.getData();
                Tarea tarea = (Tarea) intent.getSerializableExtra("tarea");
                tareaList.add(indiceTarea,tarea);
                if(tarea.isPrioritaria()){
                    tareasPreferentes.add(tarea);
                }
                updateUI();


            }
        }
    };
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("tareas", (ArrayList<? extends Parcelable>) tareaList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tareaList = savedInstanceState.getParcelableArrayList("tareas");
        tareaAdapter.setTareas(tareaList);
    }


    ActivityResultLauncher<Intent> launcher = registerForActivityResult(contract, respuesta);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);

        // Cambia el ícono según el estado de mostrarPreferentes
        MenuItem prioritariasItem = menu.findItem(R.id.prioritarias);
        if (mostrarPreferentes) {

            prioritariasItem.setIcon(R.drawable.star_filled);
        } else {

            prioritariasItem.setIcon(R.drawable.star_unfilled);
        }

        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @Nullable String key) {
        if (key.equals("criterio_key")){
            ordenarListas();
        }
        if (key.equals("ordenacion_key")) {
            cambiarOrdenListas();
        }
    }


    ActivityResultLauncher<Intent> preferenciasActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        SharedPreferences sharedPreferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);

                    }
                }
            }
    );



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
        }else if( id == R.id.estadisticas){
            mostrarDialogoEstadisticas();

        } else if (id== R.id.preferencias) {
            Intent intent = new Intent(this, PreferenciasActivity.class);
            preferenciasActivityResultLauncher.launch(intent);

        } else if (id == R.id.prioritarias) {
            mostrarPreferentes = !mostrarPreferentes; // Cambia el estado
            updateUI(); // Actualiza la vista

            // Cambia el ícono según el estado de mostrarPreferentes
            if (mostrarPreferentes) {
                viewModel.getPreferentes().observe(this, tareas -> {
                    tareaAdapter.setDatos(tareas);
                });
                item.setIcon(R.drawable.star_filled);
            } else {
                viewModel.getTareas().observe(this, tareas -> {
                    tareaAdapter.setDatos(tareas);
                });
                item.setIcon(R.drawable.star_unfilled);
            }
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

                        dialog.dismiss(); // Cierra la ventana emergente
                    }
                });

        // Mostrar el cuadro de diálogo
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void updateUI() {
        if (mostrarPreferentes) {
            viewModel.getPreferentes().observe(this, tareas -> tareaAdapter.setDatos(tareas));
        } else {
            viewModel.getTareas().observe(this, tareas -> tareaAdapter.setDatos(tareas));
        }
    }




    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        Tarea tarea = tareaAdapter.getTarea();
        if (itemId == R.id.descripcion_menu_item) {
            Intent intent = new Intent(this, DescripcionActivity.class);
            intent.putExtra("tarea", (Parcelable) tarea);
            startActivity(intent);
            Log.d("DescripcionActivity", "document_url: " + tarea.getDocument_url());
            Log.d("DescripcionActivity", "image_url: " + tarea.getImage_url());

        } else if (itemId == R.id.borrar_menu_item) {
            mostrarDialogoConfirmacionBorrado(tarea);

        }else if(itemId == R.id.editar_menu_item){
            Intent intent = new Intent(this, EditarTareaActivity.class);
            indiceTarea = tareaList.indexOf(tarea);
            intent.putExtra("tarea", (Parcelable) tarea);
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new BorrarTarea(tarea));
            launcher.launch(intent);

        }

        return super.onContextItemSelected(item);
    }


    public void mostrarDialogoEstadisticas() {
        // Consultar estadísticas desde la base de datos
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            int totalTareas = baseDatosApp.tareadao().getTotalTareas();
            int totalPreferentes = baseDatosApp.tareadao().getTotalPreferentes();
            int totalCompletadas = baseDatosApp.tareadao().getTotalCompletadas();

            // Crear el texto para mostrar en el cuadro de diálogo
            String mensaje = "Estadísticas de Tareas:\n\n";
            mensaje += "Total de tareas: " + totalTareas + "\n";
            mensaje += "Tareas preferentes: " + totalPreferentes + "\n";
            mensaje += "Tareas completadas: " + totalCompletadas;

            // Mostrar el cuadro de diálogo en el hilo principal
            String finalMensaje = mensaje;
            runOnUiThread(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Estadísticas");
                builder.setMessage(finalMensaje);
                builder.setPositiveButton("Aceptar", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            });
        });
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
                dialog.dismiss();
            }
        });

        // Agregar botón de confirmación
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Eliminar la tarea seleccionada de la lista y actualizar la UI
                baseDatosApp = BaseDatosApp.getInstance(ListadoTareas.this);
                Executor executor = Executors.newSingleThreadExecutor();
                //Creamos un objeto de la clase BorrarProducto que realiza el borrado en un hilo aparte
                executor.execute(new BorrarTarea(tarea));
                // Actualizar la interfaz de usuario
                updateUI();
                // Mostrar un Snackbar indicando que la tarea ha sido eliminada
                showSnackbar("Tarea eliminada");
            }
        });

        // Mostrar el diálogo
        builder.show();
    }

    private void showDescriptionDialog(Tarea tarea ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Descripción")
                .setMessage(tarea.getDescripcion())
                .setPositiveButton("Aceptar", null);

        if (tarea.getDocument_url() != null || tarea.getImage_url() != null) {
            // Crea una lista para almacenar los archivos
            List<File> archivos = new ArrayList<>();

            // Accede a los archivos
            if (tarea.getDocument_url() != null) {
                File documento = new File(tarea.getDocument_url());
                archivos.add(documento);
            }

            if (tarea.getImage_url() != null) {
                File imagen = new File(tarea.getImage_url());
                archivos.add(imagen);
            }

            for (File archivo : archivos) {
                builder.setIcon(getIcon(archivo));
            }
        }


        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private int getIcon(File archivo) {
        // Comprueba el tipo de archivo


        // Establece el icono
        switch (archivo.getAbsolutePath().substring(archivo.getAbsolutePath().lastIndexOf(".") + 1)) {
            case "pdf":
                return R.drawable.icons8_documento_50;
            case "docx":
                return R.drawable.icons8_documento_50;
            case "jpg":
                return R.drawable.icons8_imagen_50;
            case "png":
                return R.drawable.icons8_imagen_50;
            default:
                return 0;
        }
    }


    private void showSnackbar(String message) {
        Snackbar.make(listaTareas, message, Snackbar.LENGTH_SHORT).show();
    }

    static class BorrarTarea implements Runnable {

        private Tarea tarea;

        public BorrarTarea(Tarea tarea) {
            this.tarea = tarea;
        }

        @Override
        public void run() {
            baseDatosApp.tareadao().delete(tarea);
        }
    }


    class MostrarPreferentes implements Runnable {

        private LiveData<List<Tarea>> tareasLiveData;

        public MostrarPreferentes(LiveData<List<Tarea>> tareasLiveData) {
            this.tareasLiveData = tareasLiveData;
        }

        @Override
        public void run() {
            // Obtener la lista de tareas preferentes desde la base de datos
            baseDatosApp.tareadao().getPreferentes();

        }
    }



    class MostrarTodas implements Runnable {

        private LiveData<List<Tarea>> tareasLiveData;

        public MostrarTodas(LiveData<List<Tarea>> tareasLiveData) {
            this.tareasLiveData = tareasLiveData;
        }

        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Obtener la lista de tareas desde la base de datos
                    baseDatosApp.tareadao().getAll();
                }
            });
        }
    }


}
