package com.pablo.ducky.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad Room para multas generadas por préstamos vencidos.
 * Se crea automáticamente cuando la fecha actual supera la de vencimiento
 * de un préstamo activo.
 *
 * Tarifa: $5 MXN por día de retraso.
 */
@Entity(
    tableName = "multas",
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
data class MultaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioId: Int,
    val prestamoId: Int,
    val tituloLibro: String,
    /** Fecha en que se generó la multa (milisegundos) */
    val fechaGenerada: Long,
    /** Días de retraso al momento de generar la multa */
    val diasRetraso: Int,
    /** Monto en pesos MXN ($5 × diasRetraso) */
    val monto: Double,
    /** true = pendiente de pago, false = pagada */
    val pagada: Boolean = false
)
