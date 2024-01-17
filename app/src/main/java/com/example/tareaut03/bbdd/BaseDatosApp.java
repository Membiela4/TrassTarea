package com.example.tareaut03.bbdd;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;


import com.example.tareaut03.daos.TareaDAO;
import com.example.tareaut03.model.Tarea;

@Database(entities = {Tarea.class}, version =1, exportSchema = false)
public abstract class BaseDatosApp extends RoomDatabase {

    private static BaseDatosApp INSTANCIA;
    public static BaseDatosApp getInstance(Context context) {
        if (INSTANCIA == null) {
            INSTANCIA = Room.databaseBuilder(
                            context.getApplicationContext(),
                            BaseDatosApp.class,
                            "bdTareas")
                    .build();
        }
        return INSTANCIA;
    }

    public static void destroyInstance() {
        INSTANCIA = null;
    }

    public abstract TareaDAO tareadao();
}
