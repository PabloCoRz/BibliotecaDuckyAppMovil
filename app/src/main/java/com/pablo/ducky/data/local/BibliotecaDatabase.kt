package com.pablo.ducky.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pablo.ducky.data.local.dao.PrestamoDao
import com.pablo.ducky.data.local.dao.UsuarioDao
import com.pablo.ducky.data.local.entity.PrestamoEntity
import com.pablo.ducky.data.local.entity.UsuarioEntity

@Database(entities = [UsuarioEntity::class, PrestamoEntity::class], version = 1)
abstract class BibliotecaDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun prestamoDao(): PrestamoDao

    companion object {
        @Volatile private var INSTANCE: BibliotecaDatabase? = null

        fun getDatabase(context: Context): BibliotecaDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, BibliotecaDatabase::class.java, "biblioteca_db")
                    .build().also { INSTANCE = it }
            }
    }
}
