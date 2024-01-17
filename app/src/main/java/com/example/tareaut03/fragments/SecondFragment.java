package com.example.tareaut03.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
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
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.tareaut03.R;
import com.example.tareaut03.bbdd.BaseDatosApp;
import com.example.tareaut03.model.Tarea;
import com.example.tareaut03.utils.ConfiguracionAlmacenamiento;
import com.example.tareaut03.viewmodel.MyViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SecondFragment extends Fragment {

    private onTaskCreatedListener comunicador2;
    private TextView tvDescripcion, tvDocument, tvImage, tvAudio, tvVideo;
    private Button btnAdd, btnBack;
    private ImageButton btnDocument, btnImage, btnAudio, btnVideo;
    private MyViewModel viewModel;
    private FragmentTransaction fragmentTransaction;
    private Tarea nuevaTarea;

    String rutaDocumento,rutaImagen;

    private int archivo =0;
    BaseDatosApp baseDatosApp;

    private ActivityResultLauncher<String> documentPickerLauncher, imagePickerLauncher;

    private File destinationDirectory;

    private ActivityResultLauncher<Intent> activityResultLauncher;
    public SecondFragment() {
        destinationDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "ArchivosComunes");
    }

    public interface onTaskCreatedListener {
        void onTaskCreated();
        void onBack();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null && viewModel.getDescripcionTarea() != null && tvDescripcion != null) {
            tvDescripcion.setText(viewModel.getDescripcionTarea().getValue());

        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);

        // Obtener o crear el directorio de destino
        destinationDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "ArchivosComunes");
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdirs();
        }

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                escribirArchivos(data, archivo);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmento2 = inflater.inflate(R.layout.fragment_second, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);


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

        baseDatosApp = BaseDatosApp.getInstance(getActivity().getApplicationContext());

        btnAdd.setOnClickListener(v -> {
            viewModel.setDescripcionTarea(tvDescripcion.getText().toString());
            viewModel.setImageURL(tvImage.getText().toString());
            viewModel.setDocumentURL(tvDocument.getText().toString());

            Tarea tarea = new Tarea(
                    viewModel.getTituloTarea() != null ? viewModel.getTituloTarea().getValue() : "",
                    viewModel.getDescripcionTarea() != null ? viewModel.getDescripcionTarea().getValue() : "",
                    viewModel.getProgreso().getValue() != null ? viewModel.getProgreso().getValue() : 0,
                    viewModel.getFechaInicio() != null ? viewModel.getFechaInicio().getValue() : "",
                    viewModel.getFechaFinalizacion() != null ? viewModel.getFechaFinalizacion().getValue() : "",
                    viewModel.getTareaPrioritaria().getValue() != null ? viewModel.getTareaPrioritaria().getValue() : false,
                    viewModel.getImageURL() != null ? viewModel.getImageURL().getValue() : "",
                    viewModel.getDocumentURL() != null ? viewModel.getDocumentURL().getValue() : ""
            );

            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new InsertarTarea(tarea));


            requireActivity().finish();
        });


        btnBack.setOnClickListener(v -> {

            viewModel.setDescripcionTarea(tvDescripcion.getText().toString());
            viewModel.setImageURL(tvImage.getText().toString());
            viewModel.setDocumentURL(tvDocument.getText().toString());
            comunicador2.onBack();

        });

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


    private String getFileNameFromUri(Uri uri) {
        String fileName = null;

        // Intentar obtener el nombre del archivo desde la columna DISPLAY_NAME
        String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
        try (Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                fileName = cursor.getString(columnIndex);
            }
        }

        // Si no se pudo obtener desde DISPLAY_NAME, intentar obtener la ruta desde la URI
        if (fileName == null) {
            // En Android 10 y versiones posteriores, utiliza DocumentFile para obtener el nombre
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                DocumentFile documentFile = DocumentFile.fromSingleUri(requireActivity(), uri);
                if (documentFile != null) {
                    fileName = documentFile.getName();
                }
            }
        }

        return fileName;
    }



    public void escribirArchivos(Intent data, int caso){
        Uri imageUri = data.getData();
        // Guarda la imagen en el almacenamiento interno del dispositivo
        File imageFile = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), imageUri.getLastPathSegment());
        try {
            InputStream inputStream =  getContext().getContentResolver().openInputStream(imageUri);
            OutputStream outputStream = new FileOutputStream(imageFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        switch (caso) {
            case 1:
                rutaImagen = imageFile.getAbsolutePath();
                tvImage.setText(rutaImagen);
                break;
            case 2:
                rutaDocumento = imageFile.getAbsolutePath();
                tvDocument.setText(rutaDocumento);
                break;
        }


    }




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onTaskCreatedListener)
            comunicador2 = (SecondFragment.onTaskCreatedListener) context;
        else
            throw new ClassCastException();
    }





    class InsertarTarea implements Runnable {

        private Tarea tarea;

        public InsertarTarea(Tarea tarea) {
            this.tarea = tarea;
        }

        @Override
        public void run() {
            baseDatosApp.tareadao().insert(tarea);
        }
    }






}



