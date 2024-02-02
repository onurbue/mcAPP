package com.example.projetocma.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import models.Obras
import models.Tickets
@Dao
interface TicketDAO {
    @Insert
    fun insertTicket(tickets: Tickets)
    @Insert
    fun insertTicketsList(tickets: List<Tickets>)
    @Query("SELECT * FROM tickets")
    fun getAll(): List<Tickets>

    @Query("SELECT COUNT(*) FROM tickets LIMIT 1")
    fun hasAnyRecord(): Boolean
}