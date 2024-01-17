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
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
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


            switchOrden.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    boolean ascendente = (boolean) newValue;
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext().getApplicationContext());
                    sharedPreferences.edit().putBoolean("switch_orden_key", ascendente).apply();
                    return true;
                }
            });



            Preference buttonPreference = findPreference("boton_restablecer");
            if (buttonPreference != null) {
                buttonPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        PreferenceManager.getDefaultSharedPreferences(requireContext()).edit().clear().apply(); //establece las propiedades por defecto del boton
                        return true;
                    }
                });
            }

            if (listFontSize != null){
                listFontSize.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                        String valor = (String) newValue;
                        Context applicationContext = requireContext().getApplicationContext();
                        establecerFuente(applicationContext, valor);
                        Resources rc = getResources();
                        Configuration configuration = rc.getConfiguration();
                        DisplayMetrics dm = rc.getDisplayMetrics();

                        switch (valor) {
                            case "small":
                                configuration.fontScale = 0.75f;
                                break;
                            case "medium":
                                configuration.fontScale = 1f;
                                break;
                            case "large":
                                configuration.fontScale = 1.25f;
                                break;
                        }
                        rc.updateConfiguration(configuration, dm);
                        requireActivity().recreate();
                        return true;
                    }
                });

            }


            listCriterio.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    String marcado = (String) newValue;
                    Context applicationContext =  requireContext().getApplicationContext();

                    switch (marcado){
                        case "orden_value":
                            ordenadoTareas(applicationContext, "orden_value");
                            break;
                        case "fecha_value":
                            ordenadoTareas(applicationContext, "fecha_value");
                            break;
                        case "dias_value":
                            ordenadoTareas(applicationContext, "dias_value");
                            break;
                        case "prioritaria_value":
                            ordenadoTareas(applicationContext, "prioritaria_value");
                            break;
                    }
                    return true;
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

        public void ordenadoTareas(Context context, String estado){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            sharedPreferences.edit().putString("criterio_key", estado).apply();
        }

        private void establecerFuente(Context context, String estado) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            sharedPreferences.edit().putString("fuente_key", estado).apply();

        }

        public void setListener(PreferenciasActivity preferenciasActivity) {
            this.actividadPadre = preferenciasActivity;
        }
    }
}
