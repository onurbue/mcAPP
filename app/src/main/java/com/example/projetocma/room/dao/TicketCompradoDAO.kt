package com.example.projetocma.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import models.TicketsComprados
@Dao
interface TicketCompradoDAO {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertTicketsCompradosList(tickets: List<TicketsComprados>)
    @Query("SELECT * FROM TicketsComprados")
    fun getAll(): LiveData<List<TicketsComprados>>

    @Query("DELETE FROM TicketsComprados")
    fun clearAllData()

}