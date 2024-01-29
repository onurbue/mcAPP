package com.example.projetocma.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projetocma.room.dao.MuseuDAO
//import com.example.projetocma.room.dao.MuseuDAO
import models.Museu


@Database(entities = [Museu::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun museuDao(): MuseuDAO

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