package com.pablo.ducky.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        LibroEntity::class,
        UsuarioEntity::class,
        PrestamoEntity::class,
        MultaEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun libroDao(): LibroDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun prestamoDao(): PrestamoDao
    abstract fun multaDao(): MultaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ducky_biblioteca.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}