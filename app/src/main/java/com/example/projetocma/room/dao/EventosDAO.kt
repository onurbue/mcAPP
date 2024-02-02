package com.example.projetocma.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import models.Eventos
import models.Museu

@Dao
interface EventosDAO {

    @Insert
    fun insertEvento(evento: Eventos)
    @Insert
    fun insertEventosList(eventos: List<Eventos>)
    @Query("SELECT * FROM eventos")
    fun getAll(): List<Eventos>

    @Query("SELECT COUNT(*) FROM EVENTOS LIMIT 1")
    fun hasAnyRecord(): Boolean
}