package com.example.tareaut02.activities;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tareaut02.R;

public class PreferenciasActivity extends AppCompatActivity {

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

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    public static class FragmentoPrefencias extends PreferenceFragmentCompat{

        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
            setPreferencesFromResource(R.xml.preferencias_view, rootKey);
            CheckBoxPreference checkBoxSd = findPreference("check_sd");
            SwitchPreference switchTema = findPreference("switch_tema");
            SwitchPreference switchOrden = findPreference("switch_orden");


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

            switchOrden.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {

                        // Habilitar/deshabilitar otras preferencias según el estado del checkbox
                        boolean isChecked = (boolean) newValue;
                        findPreference("ip").setEnabled(isChecked);
                        findPreference("nombre").setEnabled(isChecked);

                        return true;
                    }
                });
            }
        }
    }


