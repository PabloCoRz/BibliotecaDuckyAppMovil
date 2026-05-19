package com.pablo.ducky.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Base de datos Room de la aplicación.
 * Singleton: se crea una sola instancia durante el ciclo de vida del proceso.
 */
@Database(
    entities = [LibroEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun libroDao(): LibroDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ducky_biblioteca.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
