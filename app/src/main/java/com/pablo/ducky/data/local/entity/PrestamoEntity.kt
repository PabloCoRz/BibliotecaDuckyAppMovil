package com.pablo.ducky.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prestamos")
data class PrestamoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val libroId: Int,
    val tituloLibro: String,
    val autorLibro: String,
    val portadaUrl: String?,
    val fechaSolicitud: Long,
    val fechaDevolucion: Long,
    val devuelto: Boolean = false
)
