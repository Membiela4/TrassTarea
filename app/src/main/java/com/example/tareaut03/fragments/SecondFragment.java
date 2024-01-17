package com.example.tareaut03.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.tareaut03.R;
import com.example.tareaut03.bbdd.BaseDatosApp;
import com.example.tareaut03.model.Tarea;
import com.example.tareaut03.viewmodel.MyViewModel;

import java.io.File;
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
    BaseDatosApp baseDatosApp;

    private ActivityResultLauncher<String> documentPickerLauncher, imagePickerLauncher;

    private File destinationDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "ArchivosComunes");;  // Directorio de destino común

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
            if (tvDocument.getText() != null) {
                viewModel.setDocumentURL(tvDocument.getText().toString());
                moveFileToDirectory(Uri.parse(tvDocument.getText().toString()));
            }
            if (tvImage.getText() != null) {
                viewModel.setImageURL(tvImage.getText().toString());
                moveFileToDirectory(Uri.parse(tvImage.getText().toString()));
            }

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


        btnBack.setOnClickListener(v -> comunicador2.onBack());

        documentPickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        tvDocument.setText(getFilePathFromUri(uri));
                    }
                });

        btnDocument.setOnClickListener(v -> documentPickerLauncher.launch("*/*"));

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        tvImage.setText(getFilePathFromUri(uri));
                    }
                });

        btnImage.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        return fragmento2;
    }

    private void moveFileToDirectory(Uri sourceUri) {
        try {
            // Get the path of the file
            String filePath = getFilePathFromUri(sourceUri);

            if (filePath != null) {
                // Verify if the SD card is available
                if (isExternalStorageWritable()) {
                    // Create an InputStream from the source URI
                    try (InputStream inputStream = requireActivity().getContentResolver().openInputStream(sourceUri)) {
                        // Check if the target directory exists
                        if (!destinationDirectory.exists()) {
                            Toast.makeText(requireActivity(), "Target directory does not exist", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Create a new file in the target directory
                        File destinationFile = new File(destinationDirectory, new File(filePath).getName());

                        // Open an OutputStream to the new file
                        try (OutputStream outputStream = new FileOutputStream(destinationFile)) {
                            // Copy the data from the InputStream to the OutputStream
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = inputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, length);
                            }

                            Toast.makeText(requireActivity(), "File moved successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // The SD card is not available
                    Toast.makeText(requireActivity(), "SD card not available", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Unable to get the file path
                Toast.makeText(requireActivity(), "Unable to get file path", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireActivity(), "Error moving file", Toast.LENGTH_SHORT).show();
        }
    }


    // Método para verificar si la tarjeta SD está disponible y se puede escribir
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private String getFilePathFromUri(Uri uri) {
        String filePath = null;

        // Intentar obtener la ruta del archivo desde la columna DATA
        String[] projection = {MediaStore.Images.Media.DATA};
        try (Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                filePath = cursor.getString(columnIndex);
            }
        }

        // Si no se pudo obtener desde DATA, intentar obtener la ruta desde la URI
        if (filePath == null) {
            filePath = uri.getPath();
        }

        return filePath;
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



