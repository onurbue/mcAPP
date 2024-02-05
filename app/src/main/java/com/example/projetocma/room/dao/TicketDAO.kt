package com.example.projetocma.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import models.Obras
import models.Tickets
@Dao
interface TicketDAO {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertTicketsList(tickets: List<Tickets>)
    @Query("SELECT * FROM tickets WHERE museum_id = :museuId")
    fun getMuseumTickets(museuId: String): LiveData<List<Tickets>>

    @Query("DELETE FROM Tickets")
    fun clearAllData()

}
