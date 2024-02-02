package com.example.projetocma.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projetocma.room.dao.EventosDAO
import com.example.projetocma.room.dao.MuseuDAO
import com.example.projetocma.room.dao.ObrasDAO
import com.example.projetocma.room.dao.TicketCompradoDAO
import com.example.projetocma.room.dao.TicketDAO
import models.Eventos
//import com.example.projetocma.room.dao.MuseuDAO
import models.Museu
import models.Obras
import models.Tickets
import models.TicketsComprados


@Database(entities = [Museu::class, Eventos::class, Obras::class, Tickets::class, TicketsComprados::class], version = 5, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun museuDao(): MuseuDAO
    abstract fun eventosDao(): EventosDAO
    abstract fun obrasDao(): ObrasDAO
    abstract fun ticketsDao(): TicketDAO
    abstract fun ticketsCompradosDao(): TicketCompradoDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "museu_connect"
                        ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
                    }
                }
            }
            return INSTANCE
        }
    }
}