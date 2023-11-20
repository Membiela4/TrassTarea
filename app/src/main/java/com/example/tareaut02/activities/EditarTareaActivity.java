package com.example.tareaut02.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.tareaut02.R;
import com.example.tareaut02.fragments.FirstFragment;
import com.example.tareaut02.fragments.SecondFragment;
import com.example.tareaut02.viewmodel.MyViewModel;

public class EditarTareaActivity extends AppCompatActivity {

    MyViewModel viewModel;
    FirstFragment firstFragment;
    SecondFragment secondFragment;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_tarea);
        viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        firstFragment = new FirstFragment();
        secondFragment = new SecondFragment();
        fragmentManager.beginTransaction().add(R.id.fragment_container_editar,firstFragment).commit();
    }
}