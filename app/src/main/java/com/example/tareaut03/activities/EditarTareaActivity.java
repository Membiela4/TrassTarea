package com.example.tareaut03.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.tareaut03.R;
import com.example.tareaut03.bbdd.BaseDatosApp;
import com.example.tareaut03.fragments.FirstFragment;
import com.example.tareaut03.fragments.SecondFragment;
import com.example.tareaut03.model.Tarea;
import com.example.tareaut03.viewmodel.MyViewModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditarTareaActivity extends AppCompatActivity implements SecondFragment.onTaskCreatedListener, FirstFragment.OnNextBtnClicked {

    MyViewModel vm;
    FirstFragment firstFragment;
    SecondFragment secondFragment;
    FragmentManager fragmentManager;

    BaseDatosApp baseDatosApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_tarea);

        baseDatosApp = BaseDatosApp.getInstance(this);

        vm = new ViewModelProvider(this).get(MyViewModel.class);
        firstFragment = new FirstFragment();
        secondFragment = new SecondFragment();
        fragmentManager = getSupportFragmentManager();

        if (getIntent().hasExtra("tarea")) {
            Tarea tarea = getIntent().getParcelableExtra("tarea");
            if (tarea != null) {
                // Configurar los valores en el ViewModel
                vm.setTituloTarea(tarea.getTitulo());
                vm.setFechaInicio(tarea.getFechaInicio());
                vm.setPrioritaria(tarea.isPrioritaria());
                vm.setProgreso(tarea.getProgreso());
                vm.setFechaFinalizacion(tarea.getFechaFinal());
                vm.setDescripcionTarea(tarea.getDescripcion());
                vm.setDocumentURL(tarea.getDocument_url());
                vm.setImageURL(tarea.getImage_url());
            }
        }

        // Asegúrate de que el primer fragmento esté añadido solo si savedInstanceState es nulo
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().add(R.id.fragment_container_editar, firstFragment).commit();

        }

    }




    @Override
    public void onTaskCreated() {
        // Aquí debes obtener la información de la tarea desde tus vistas (EditText, etc.)
        Tarea tareaOriginal = getIntent().getParcelableExtra("tarea");

        // Eliminar la tarea original de la base de datos
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            baseDatosApp.tareadao().delete(tareaOriginal);

            // Crear la nueva tarea editada
            Tarea newTask = new Tarea(vm.getTituloTarea(), vm.getDescripcionTarea(), vm.getProgreso(), vm.getFechaInicio(),
                    vm.getFechaFinalizacion(), vm.getTareaPrioritaria());

            // Llamada al método de la interfaz para pasar la nueva tarea a la actividad
            Intent intent = new Intent(this, ListadoTareas.class);
            intent.putExtra("tarea", (Parcelable) newTask);

            if (newTask != null) {
                runOnUiThread(() -> {
                    setResult(Activity.RESULT_OK, intent);
                    executor.execute(new EditarTarea(newTask));

                });
                finish();
            }
        });
    }



    @Override
    public void onBack() {
        // Verifica si el segundo fragmento está en la pila de fragmentos antes de realizar la transacción
        if (secondFragment != null && secondFragment.isAdded()) {
            // Si el segundo fragmento está agregado, realiza una transacción para mostrar el primer fragmento
            fragmentManager.beginTransaction().replace(R.id.fragment_container_editar, firstFragment).commit();
        } else {
            // Si el segundo fragmento no está agregado, muestra un log o realiza alguna acción de depuración
            Log.e(TAG, "onBack: SecondFragment no está agregado");
        }
    }

    @Override
    public void btnSiguiente() {
        // Verifica si el segundo fragmento no está en la pila de fragmentos antes de realizar la transacción
        if (secondFragment != null && !secondFragment.isAdded()) {
            fragmentManager.beginTransaction().replace(R.id.fragment_container_editar, secondFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void btnCancelar() {
        finish();
    }


    class EditarTarea implements Runnable {

        private Tarea tarea;

        public EditarTarea(Tarea tarea) {
            this.tarea = tarea;
        }

        @Override
        public void run() {
            baseDatosApp.tareadao().update(tarea);
        }
    }

}

