package com.example.tareaut03.activities;






import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.tareaut03.R;
import com.example.tareaut03.adapters.TareaAdapter;
import com.example.tareaut03.bbdd.BaseDatosApp;
import com.example.tareaut03.model.Tarea;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PreferenciasActivity extends AppCompatActivity {

    Intent resultado;

    static TareaAdapter tareaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        if(tareaAdapter==null){
            tareaAdapter = new TareaAdapter(this,new ArrayList<Tarea>());
        }

        FragmentoPrefencias fragment = new FragmentoPrefencias();
        fragment.setListener(this);
        getSupportFragmentManager().beginTransaction()
                //El recurso 'android.R.id.content' es la ventana activa de la aplicación
                .replace(android.R.id.content, fragment)
                .commit();
        resultado = new Intent();

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK, resultado);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSwitchChanged(boolean newValue) {
        // Obtener el estado del switch
        boolean isChecked = newValue;

        // Notificar a la actividad principal
        if (isChecked) {
            resultado.putExtra("temaOscuro", true);
        } else {
            resultado.putExtra("temaOscuro", false);
        }
    }



    public static class FragmentoPrefencias extends PreferenceFragmentCompat {

        private AppCompatActivity actividadPadre;

        BaseDatosApp baseDatosApp = BaseDatosApp.getInstance(this.getContext());
        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
            setPreferencesFromResource(R.xml.preferencias_view, rootKey);
            actividadPadre = (AppCompatActivity) getActivity();

            CheckBoxPreference checkBoxSd = findPreference("check_sd");
            EditTextPreference editTextPreference = findPreference("numdias_limpieza");
            SwitchPreference switchTema = findPreference("switch_tema");
            ListPreference listFontSize = findPreference("fuente_key");
            ListPreference listCriterio = findPreference("criterio_key");
            SwitchPreference switchOrden = findPreference("switch_orden_key");
            SwitchPreference switchBbdd = findPreference("switch_bbdd");
            findPreference("ip").setEnabled(false);
            findPreference("nombre").setEnabled(false);
            findPreference("puerto").setEnabled(false);
            findPreference("usuario").setEnabled(false);
            findPreference("contrasenya").setEnabled(false);



            Preference buttonPreference = findPreference("boton_restablecer");
            if (buttonPreference != null) {
                buttonPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        // Realiza la acción deseada al hacer clic en el botón
                        //metodo para restablecer las preferencias
                        return true;
                    }
                });
            }

            listFontSize.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    // Obtener el nuevo valor del tamaño de la fuente
                    String newFontSize = (String) newValue;

                    // Cambiar el tamaño de fuente de la preferencia
                    float fontSize = getResources().getConfiguration().fontScale;
                    switch (newFontSize) {
                        case "small":
                            fontSize = getResources().getDimension(R.dimen.font_size_small);
                            break;
                        case "medium":
                            fontSize = getResources().getDimension(R.dimen.font_size_medium);
                            break;
                        case "large":
                            fontSize = getResources().getDimension(R.dimen.font_size_large);
                            break;
                    }

                    listFontSize.setValue(Float.toString(fontSize));


                    return true;
                }
            });


            listCriterio.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {

                    actualizarListaTareas((String) newValue);

                    return false;
                }
            });


            switchBbdd.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {

                    // Habilitar/deshabilitar otras preferencias según el estado del checkbox
                    boolean isChecked = (boolean) newValue;
                    findPreference("ip").setEnabled(isChecked);
                    findPreference("nombre").setEnabled(isChecked);
                    findPreference("puerto").setEnabled(isChecked);
                    findPreference("usuario").setEnabled(isChecked);
                    findPreference("contrasenya").setEnabled(isChecked);

                    return true;
                }
            });

            switchTema.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    // Obtener el estado del switch
                    boolean value = (boolean) newValue;
                    Context applicationContext = requireContext().getApplicationContext();
                    aplicarTema(applicationContext, value);
                    if (value) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }

                    return true;
                }
            });
        }

        private void aplicarTema(Context context, boolean isDarkThemeEnabled) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            sharedPreferences.edit().putBoolean("switch_tema", isDarkThemeEnabled).apply();
        }


        private void actualizarListaTareas(String selectedValue) {
            Executor executor = Executors.newSingleThreadExecutor();

            executor.execute(() -> {
                List<Tarea> tareas;

                // Realiza la consulta según el valor selec  cionado
                switch (selectedValue) {
                    case "fecha_value": // Por fecha
                        tareas = baseDatosApp.tareadao().getTareasOrderByFecha();
                        break;
                    case "prioritaria_value": // Por prioritarias
                        tareas = baseDatosApp.tareadao().getTareasOrderByPrioritarias();
                        break;
                    case "orden_value": // Por titulo alfabetico
                        tareas = baseDatosApp.tareadao().getTareasbyOrdenAlfabetico();
                        break;
                    default:
                        // Valor por defecto o manejo de otro caso
                        tareas = baseDatosApp.tareadao().getTareasbyOrdenAlfabetico();
                        break;
                }

                // Actualiza la interfaz de usuario en el hilo principal
                requireActivity().runOnUiThread(() -> {
                    // Actualiza la lista de tareas en la interfaz de usuario con la nueva lista ordenada
                    // (Puedes utilizar la lógica adecuada según tu adaptador y diseño)

                    tareaAdapter.setDatos(tareas);
                    tareaAdapter.notifyDataSetChanged();
                });
            });
        }

        public void setListener(PreferenciasActivity preferenciasActivity) {
            this.actividadPadre = preferenciasActivity;
        }
    }
}
