package com.example.tareaut02.activities;




import static com.example.tareaut02.activities.PreferenciasActivity.FragmentoPrefencias.*;
import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tareaut02.R;

public class PreferenciasActivity extends AppCompatActivity     {

    Intent resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Preferencias de usuario");
        getSupportFragmentManager().beginTransaction()
                //El recurso 'android.R.id.content' es la ventana activa de la aplicación
                .replace(android.R.id.content, new FragmentoPrefencias())
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

        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
            setPreferencesFromResource(R.xml.preferencias_view, rootKey);
            CheckBoxPreference checkBoxSd = findPreference("check_sd");
            SwitchPreference switchTema = findPreference("switch_tema");
            SwitchPreference switchOrden = findPreference("switch_orden");
            SwitchPreference switchBbdd = findPreference("switch_bbdd");

            actividadPadre = (AppCompatActivity) getActivity();


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

        }
    }
}