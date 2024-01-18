    package com.example.tareaut03.activities;

    import static android.app.PendingIntent.getActivity;
    import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.fragment.app.FragmentManager;
    import androidx.lifecycle.ViewModelProvider;
    import androidx.recyclerview.widget.LinearLayoutManager;

    import android.app.Activity;
    import android.content.Intent;
    import android.os.Bundle;
    import android.os.Parcelable;
    import android.text.TextUtils;
    import android.util.Log;
    import android.widget.Toast;

    import com.example.tareaut03.R;
    import com.example.tareaut03.adapters.TareaAdapter;
    import com.example.tareaut03.bbdd.BaseDatosApp;
    import com.example.tareaut03.fragments.FirstFragment;
    import com.example.tareaut03.fragments.SecondFragment;
    import com.example.tareaut03.model.Tarea;
    import com.example.tareaut03.viewmodel.MyViewModel;

    import java.util.ArrayList;

    public class CrearTareaActivity extends AppCompatActivity implements SecondFragment.onTaskCreatedListener, FirstFragment.OnNextBtnClicked {

        MyViewModel vm;
        FirstFragment firstFragment;
        SecondFragment secondFragment;
        FragmentManager fragmentManager;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_crear_tarea);

            vm = new MyViewModel(this.getApplication());

            firstFragment = new FirstFragment();
            secondFragment = new SecondFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.fragment_container_editar,firstFragment).commit();

        }

        @Override
        public void onTaskCreated() {


                Tarea newTask = new Tarea(vm.getTituloTarea(), vm.getDescripcionTarea(), vm.getProgreso(), vm.getFechaInicio()
                        , vm.getFechaFinalizacion(), vm.getTareaPrioritaria(),vm.getImageURL(),vm.getDocumentURL(),vm.getVideoURL(),vm.getAudioURL());



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
             
                fragmentManager.beginTransaction().replace(R.id.fragment_container_editar, firstFragment).commit();
            } else {
                // Si el segundo fragmento no está agregado, muestra un log o realiza alguna acción de depuración
                Log.e(TAG, "onBack: SecondFragment no está agregado");
            }
        }

        @Override
        public void btnSiguiente() {
            // Obtener los valores actuales de los campos desde el ViewModel
            String tituloTarea = vm.getTituloTarea().getValue();
            String fechaInicio = vm.getFechaInicio().getValue();
            String fechaObjetivo = vm.getFechaFinalizacion().getValue();

            // Verificar si los campos están vacíos

                // Realizar la transacción del fragmento solo si los campos no están vacíos
                if (!secondFragment.isAdded()) {
                    fragmentManager.beginTransaction().replace(R.id.fragment_container_editar, secondFragment).commit();
                }

        }

        @Override
        public void btnCancelar() {
            finish();
        }
    }


