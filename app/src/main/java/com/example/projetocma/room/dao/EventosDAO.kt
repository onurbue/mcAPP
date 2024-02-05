package com.example.projetocma.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import models.Eventos
import models.Museu

@Dao
interface EventosDAO {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertEventosList(eventos: List<Eventos>)
    @Query("SELECT * FROM eventos WHERE museu_id = :museuId")
    fun getMuseumEvents(museuId : String): LiveData<List<Eventos>>

}