package com.example.projetocma.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import models.Museu

@Dao
interface MuseuDAO {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertMuseuList(museus: List<Museu>)
    @Query("SELECT * FROM museu")
    fun getAll(): LiveData<List<Museu>>

}