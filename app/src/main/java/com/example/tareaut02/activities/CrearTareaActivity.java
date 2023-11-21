    package com.example.tareaut02.activities;

    import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.fragment.app.FragmentManager;
    import androidx.lifecycle.ViewModelProvider;

    import android.app.Activity;
    import android.content.Intent;
    import android.os.Bundle;
    import android.os.Parcelable;
    import android.util.Log;

    import com.example.tareaut02.R;
    import com.example.tareaut02.fragments.FirstFragment;
    import com.example.tareaut02.fragments.SecondFragment;
    import com.example.tareaut02.model.Tarea;
    import com.example.tareaut02.viewmodel.MyViewModel;

    public class CrearTareaActivity extends AppCompatActivity implements SecondFragment.onTaskCreatedListener, FirstFragment.OnNextBtnClicked {

        MyViewModel vm;
        FirstFragment firstFragment;
        SecondFragment secondFragment;
        FragmentManager fragmentManager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_crear_tarea);
            vm = new ViewModelProvider(this).get(MyViewModel.class);
            firstFragment = new FirstFragment();
            secondFragment = new SecondFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.fragment_container_editar,firstFragment).commit();

        }

        @Override
        public void onTaskCreated() {
            // Aquí debes obtener la información de la tarea desde tus vistas (EditText, etc.)
             Tarea newTask = new Tarea(vm.getTituloTarea(), vm.getDescripcionTarea(), vm.getProgreso(), vm.getFechaInicio()
                     , vm.getFechaFinalizacion(), vm.getTareaPrioritaria());
            // Llamada al método de la interfaz para pasar la nueva tarea a la actividad
            Intent intent = new Intent(this,ListadoTareas.class);
            intent.putExtra("tarea", (Parcelable) newTask);
            if(newTask!=null){
                setResult(Activity.RESULT_OK,intent);
                finish();
            }

        }

        @Override
        public void onBack() {
            if (secondFragment.isAdded()) {
                // Si el segundo fragmento está agregado, realiza una transacción para mostrar el primer fragmento
                fragmentManager.beginTransaction().replace(R.id.fragment_container_editar, firstFragment).commit();
            } else {
                // Si el segundo fragmento no está agregado, muestra un log o realiza alguna acción de depuración
                Log.e(TAG, "onBack: SecondFragment no está agregado");
            }
        }

        @Override
        public void btnSiguiente() {
            if (!secondFragment.isAdded())
                fragmentManager.beginTransaction().replace(R.id.fragment_container_editar,secondFragment).commit();
        }

        @Override
        public void btnCancelar() {
            finish();
        }
    }