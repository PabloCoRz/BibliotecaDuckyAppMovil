package com.pablo.ducky.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.pablo.ducky.data.local.entity.UsuarioEntity

@Dao
interface UsuarioDao {
    @Insert
    suspend fun insert(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): UsuarioEntity?
}
