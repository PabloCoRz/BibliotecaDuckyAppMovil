package com.pablo.ducky.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad Room para préstamos de libros.
 * Un préstamo pertenece a un usuario y a un libro.
 * Las fechas se almacenan como milisegundos (Long) para facilitar comparaciones.
 */
@Entity(
    tableName = "prestamos",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("usuarioId")]
)
data class PrestamoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioId: Int,
    val libroId: Int,
    val tituloLibro: String,
    val autorLibro: String,
    val portadaUrl: String?,
    /** Fecha de solicitud en milisegundos */
    val fechaSolicitud: Long,
    /** Fecha de vencimiento en milisegundos (solicitud + 8 días) */
    val fechaVencimiento: Long,
    /** true = activo, false = devuelto */
    val activo: Boolean = true
)
