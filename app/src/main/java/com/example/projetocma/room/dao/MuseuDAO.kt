package com.example.projetocma.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import models.Museu

@Dao
interface MuseuDAO {

    @Insert
    fun insertMuseu(museu: Museu)
    @Insert
    fun insertMuseuList(museus: List<Museu>)
    @Query("SELECT * FROM museu")
    fun getAll(): List<Museu>

    @Query("SELECT COUNT(*) FROM museu LIMIT 1")
    fun hasAnyRecord(): Boolean
}