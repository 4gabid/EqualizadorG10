package com.grupo10.equalizadorg10.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Profile::class], version = 2, exportSchema = false) // Versão 2
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "equalizer_database"
                ).fallbackToDestructiveMigration() // Essa linha permite recriar o banco ao atualizar a versão
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
