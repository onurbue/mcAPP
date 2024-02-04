package com.example.projetocma.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import models.Obras

@Dao
 interface ObrasDAO {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertObrasList(obra: List<Obras>)
    @Query("SELECT * FROM obras")
    fun getAll(): LiveData<List<Obras>>


}
