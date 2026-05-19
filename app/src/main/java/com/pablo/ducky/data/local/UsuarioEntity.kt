package com.pablo.ducky.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad Room para usuarios registrados.
 * El email es único — no se puede registrar el mismo correo dos veces.
 */
@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["email"], unique = true)]
)
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val email: String,
    /** Contraseña hasheada (SHA-256) — nunca texto plano */
    val passwordHash: String
)
