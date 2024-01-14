package com.example.tareaut02.fragments;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.tareaut02.R;
import com.example.tareaut02.model.Tarea;
import com.example.tareaut02.viewmodel.MyViewModel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SecondFragment extends Fragment {

    private onTaskCreatedListener comunicador2;
    private TextView tvDescripcion, tvDocument, tvImage, tvAudio, tvVideo;
    private Button btnAdd, btnBack;
    private ImageButton btnDocument, btnImage, btnAudio, btnVideo;
    private MyViewModel sharedViewModel;
    private FragmentTransaction fragmentTransaction;
    private Tarea nuevaTarea;

    private ActivityResultLauncher<String> documentPickerLauncher, imagePickerLauncher;

    private File destinationDirectory;  // Directorio de destino común

    public SecondFragment() {
        // Required empty public constructor
    }

    public interface onTaskCreatedListener {
        void onTaskCreated();
        void onBack();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedViewModel != null && sharedViewModel.getDescripcionTarea() != null)
            tvDescripcion.setText(sharedViewModel.getDescripcionTarea().getValue());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);

        // Obtener o crear el directorio de destino
        destinationDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "ArchivosComunes");
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdirs();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmento2 = inflater.inflate(R.layout.fragment_second, container, false);

        btnAdd = fragmento2.findViewById(R.id.btn_add);
        btnBack = fragmento2.findViewById(R.id.btn_back);
        tvDescripcion = fragmento2.findViewById(R.id.tv_descripcion);
        tvDocument = fragmento2.findViewById(R.id.tv_document);
        tvImage = fragmento2.findViewById(R.id.tv_image);
        tvAudio = fragmento2.findViewById(R.id.tv_audio);
        tvVideo = fragmento2.findViewById(R.id.tv_video);
        btnDocument = fragmento2.findViewById(R.id.btn_document);
        btnImage = fragmento2.findViewById(R.id.btn_image);
        btnAudio = fragmento2.findViewById(R.id.btn_audio);
        btnVideo = fragmento2.findViewById(R.id.btn_video);

        btnAdd.setOnClickListener(v -> {
            sharedViewModel.setDescripcionTarea(tvDescripcion.getText().toString());
            if (tvDocument.getText() != null) {
                sharedViewModel.setDocumentURL(tvDocument.getText().toString());
                moveFileToDirectory(Uri.parse(tvDocument.getText().toString()));
            }
            if (tvImage.getText() != null) {
                sharedViewModel.setImageURL(tvImage.getText().toString());
                moveFileToDirectory(Uri.parse(tvImage.getText().toString()));
            }
            comunicador2.onTaskCreated();
        });

        btnBack.setOnClickListener(v -> comunicador2.onBack());

        documentPickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        tvDocument.setText(getFileNameFromUri(uri));
                    }
                });

        btnDocument.setOnClickListener(v -> documentPickerLauncher.launch("*/*"));

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        tvImage.setText(getFileNameFromUri(uri));
                    }
                });

        btnImage.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        return fragmento2;
    }

    private void moveFileToDirectory(Uri sourceUri) {
        try {
            // Obtener el nombre del archivo
            String fileName = getFileNameFromUri(sourceUri);

            if (fileName != null) {
                // Crear un InputStream desde la URI de origen
                try (InputStream inputStream = requireActivity().getContentResolver().openInputStream(sourceUri)) {
                    // Crear un nuevo archivo en el directorio de destino
                    File destinationFile = new File(destinationDirectory, fileName);

                    // Abrir un OutputStream hacia el nuevo archivo
                    try (OutputStream outputStream = requireActivity().getContentResolver().openOutputStream(Uri.fromFile(destinationFile))) {
                        // Copiar los datos del InputStream al OutputStream
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                    }

                    Toast.makeText(requireActivity(), "Archivo movido con éxito", Toast.LENGTH_SHORT).show();
                }
            } else {
                // No se pudo obtener el nombre del archivo
                Toast.makeText(requireActivity(), "No se pudo obtener el nombre del archivo", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireActivity(), "Error al mover el archivo", Toast.LENGTH_SHORT).show();
        }
    }


    private String getFileNameFromUri(Uri uri) {
        String displayName = null;

        // Intentar obtener el nombre del archivo desde la columna DISPLAY_NAME
        String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
        try (Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                displayName = cursor.getString(columnIndex);
            }
        }

        // Si no se pudo obtener desde DISPLAY_NAME, intentar obtener el último segmento de la ruta
        if (displayName == null) {
            displayName = uri.getLastPathSegment();
        }

        return displayName;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onTaskCreatedListener)
            comunicador2 = (SecondFragment.onTaskCreatedListener) context;
        else
            throw new ClassCastException();
    }
}
