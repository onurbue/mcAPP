package com.example.projetocma.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import models.Tickets
import models.TicketsComprados
@Dao
interface TicketCompradoDAO {

    @Insert
    fun insertTicketComprado(tickets: TicketsComprados)
    @Insert
    fun insertTicketsCompradosList(tickets: List<TicketsComprados>)
    @Query("SELECT * FROM TicketsComprados")
    fun getAll(): List<TicketsComprados>

    @Query("SELECT COUNT(*) FROM ticketscomprados LIMIT 1")
    fun hasAnyRecord(): Boolean
}