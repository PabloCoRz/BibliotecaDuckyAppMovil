package com.pablo.ducky.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsuarioDao {

    /** Registra un usuario nuevo. Lanza excepción si el email ya existe. */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registrar(usuario: UsuarioEntity): Long

    /** Busca un usuario por email y hash de contraseña (para login). */
    @Query("SELECT * FROM usuarios WHERE email = :email AND passwordHash = :hash LIMIT 1")
    suspend fun login(email: String, hash: String): UsuarioEntity?

    /** Verifica si ya existe un usuario con ese email. */
    @Query("SELECT COUNT(*) FROM usuarios WHERE email = :email")
    suspend fun existeEmail(email: String): Int

    /** Obtiene un usuario por su ID. */
    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): UsuarioEntity?
}
