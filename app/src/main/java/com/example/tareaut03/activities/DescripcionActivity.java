package com.example.tareaut03.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tareaut03.R;
import com.example.tareaut03.model.Tarea;

public class DescripcionActivity extends AppCompatActivity {

    TextView txtDescripcion, txtImagen,txtDocumento, archivosTextView, txtVideo, txtAudio;
    Button btnVolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion);

         txtDescripcion = findViewById(R.id.descripcion_text_view);
         txtDocumento = findViewById(R.id.txt_document);
         txtImagen = findViewById(R.id.txt_image);
         txtVideo = findViewById(R.id.txt_video);
         txtAudio = findViewById(R.id.txt_audio);
         archivosTextView = findViewById(R.id.archivosTextView);
         btnVolver = findViewById(R.id.btnVolver);

         btnVolver.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 finish();
             }
         });

        Tarea tarea = (Tarea) getIntent().getSerializableExtra("tarea");
        mostrarDetalles(tarea);
    }

    private void mostrarDetalles(Tarea tarea) {

        txtDescripcion.setText(tarea.getDescripcion());

        if (tarea.getDocument_url() != null || tarea.getImage_url() != null) {


            if (tarea.getDocument_url() != null) {
                txtDocumento.setText(tarea.getDocument_url().toString());
            }

            if (tarea.getImage_url() != null) {
                txtImagen.setText(tarea.getImage_url().toString());
            }

            if(tarea.getAudio_url()!=null){
                txtAudio.setText(tarea.getAudio_url().toString());
            }
            if(tarea.getVideo_url()!=null){
                txtVideo.setText(tarea.getVideo_url().toString());
            }

        } else {
            archivosTextView.setText("No hay archivos asociados.");
        }
    }

}