package com.pablo.ducky.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room que persiste los datos de un libro en la base de datos local.
 * Almacena los campos planos del modelo de red para evitar serialización compleja.
 */
@Entity(tableName = "libros")
data class LibroEntity(
    @PrimaryKey val id: Int,
    val isbn: String,
    val titulo: String,
    val subtitulo: String?,
    val editorial: String?,
    val edicion: String?,
    val anioPub: Int?,
    val numPaginas: Int?,
    val categoria: String?,
    val idioma: String?,
    val descripcion: String?,
    val portadaUrl: String?,
    /** Autores serializados como "Apellido, Nombre; Apellido2, Nombre2" */
    val autoresStr: String,
    /** Total de copias del libro */
    val totalCopias: Int,
    /** Copias con estado "Disponible" */
    val copiasDisponibles: Int,
    /** Pasillo de la primera copia */
    val pasillo: String?,
    /** Estante de la primera copia */
    val estante: String?
)
