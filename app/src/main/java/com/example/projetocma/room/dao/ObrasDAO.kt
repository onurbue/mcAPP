package com.example.projetocma.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import models.Obras

@Dao
 interface ObrasDAO {
    @Insert
    fun insertObra(obra: Obras)
    @Insert
    fun insertObrasList(obra: List<Obras>)
    @Query("SELECT * FROM obras")
    fun getAll(): List<Obras>

    @Query("SELECT COUNT(*) FROM obras LIMIT 1")
    fun hasAnyRecord(): Boolean

}
