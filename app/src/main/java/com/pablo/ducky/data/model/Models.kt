package com.pablo.ducky.data.model

data class AutorWrapper(val autor: Autor)

data class Autor(
    val id: Int,
    val nombre: String
)

data class Copia(
    val id: Int,
    val estado: String,
    val pasillo: String?,
    val estante: String?
)

data class Libro(
    val id: Int,
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
    val autores: List<AutorWrapper>,
    val copias: List<Copia>
) {
    fun autoresString() = autores.joinToString(", ") { it.autor.nombre }
    fun disponibles() = copias.count { it.estado == "Disponible" }
    fun estaDisponible() = disponibles() > 0
}