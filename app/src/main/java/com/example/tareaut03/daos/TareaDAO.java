package com.example.tareaut03.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tareaut03.model.Tarea;

import java.util.List;

@Dao
public interface TareaDAO {
    @Query("SELECT * FROM tareas")
    LiveData<List<Tarea>> getAll();


    @Query("SELECT * FROM tareas WHERE id IN (:tareasIds)")

    List<Tarea> loadAllByIds(int[] tareasIds);

    @Query("SELECT * FROM tareas WHERE prioritaria = 'true'" )
    LiveData<List<Tarea>> getPreferentes();

    @Insert
    void insert(Tarea tarea);


    @Delete
    void delete(Tarea tarea);

    @Update
    void update(Tarea tarea);
    @Query("SELECT COUNT(*) FROM tareas")
    int getTotalTareas();

    @Query("SELECT COUNT(*) FROM tareas WHERE prioritaria = 1")
    int getTotalPreferentes();

    @Query("SELECT COUNT(*) FROM tareas WHERE progreso = 100")
    int getTotalCompletadas();

    @Query("SELECT * FROM tareas ORDER BY fecha_inicio")
    LiveData<List<Tarea>> getTareasOrderByFecha();

    @Query("SELECT * FROM tareas ORDER BY prioritaria")
    List<Tarea> getTareasOrderByPrioritarias();

    @Query("SELECT * FROM tareas ORDER BY titulo")
    List<Tarea> getTareasbyOrdenAlfabetico();
    @Query("SELECT * FROM tareas ORDER BY progreso")
    List<Tarea> getTareasbyProgreso();
}
