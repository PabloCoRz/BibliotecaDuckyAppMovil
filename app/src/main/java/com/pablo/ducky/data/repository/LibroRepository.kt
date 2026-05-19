package com.pablo.ducky.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.pablo.ducky.data.local.AppDatabase
import com.pablo.ducky.data.local.LibroEntity
import com.pablo.ducky.data.model.Autor
import com.pablo.ducky.data.model.AutorWrapper
import com.pablo.ducky.data.model.Copia
import com.pablo.ducky.data.model.Libro
import com.pablo.ducky.data.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio único de datos de libros.
 *
 * Estrategia:
 *  1. Al iniciar, intenta sincronizar con la API remota y guarda en Room.
 *  2. Si no hay red, sirve los datos que ya están en Room (cache offline).
 *  3. Toda la UI observa LiveData de Room → se actualiza reactivamente.
 */
class LibroRepository(context: Context) {

    private val dao = AppDatabase.getInstance(context).libroDao()

    // ─── Observables para la UI ─────────────────────────────────────────────

    /** LiveData de búsqueda reactiva desde Room. */
    fun buscarLibrosLive(query: String): LiveData<List<Libro>> =
        dao.buscarLibros(query).map { entities -> entities.map { it.toModelo() } }

    /** LiveData con todos los libros (para la pantalla de landing). */
    fun obtenerTodosLive(): LiveData<List<Libro>> =
        dao.obtenerTodos().map { entities -> entities.map { it.toModelo() } }

    // ─── Consulta puntual ───────────────────────────────────────────────────

    suspend fun obtenerPorId(id: Int): Libro? = withContext(Dispatchers.IO) {
        dao.obtenerPorId(id)?.toModelo()
    }

    // ─── Sincronización con API ─────────────────────────────────────────────

    /**
     * Intenta obtener los libros desde la API y los persiste en Room.
     * Si la red falla pero ya hay datos en BD, continúa sin error visible.
     * Si no hay datos en BD ni red, carga el dataset local de respaldo.
     */
    suspend fun sincronizar(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val librosRemotos = RetrofitInstance.api.buscarLibros("")
            val entities = librosRemotos.map { it.toEntity() }
            dao.insertarTodos(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            // Sin red: si la BD tiene datos, es OK
            val count = dao.contarLibros()
            if (count > 0) {
                Result.success(Unit)
            } else {
                // BD vacía y sin red → cargar datos de respaldo
                dao.insertarTodos(librosFallback())
                Result.success(Unit)
            }
        }
    }

    // ─── Conversiones ───────────────────────────────────────────────────────

    private fun LibroEntity.toModelo(): Libro {
        val autores = autoresStr.split(";")
            .filter { it.isNotBlank() }
            .mapIndexed { i, nombre ->
                AutorWrapper(Autor(i, nombre.trim()))
            }
        val copias = buildList {
            repeat(copiasDisponibles) { i ->
                add(Copia(i, "Disponible", pasillo, estante))
            }
            val noDisponibles = totalCopias - copiasDisponibles
            repeat(noDisponibles) { i ->
                add(Copia(copiasDisponibles + i, "Prestada", pasillo, estante))
            }
        }
        return Libro(
            id, isbn, titulo, subtitulo, editorial, edicion,
            anioPub, numPaginas, categoria, idioma, descripcion,
            portadaUrl, autores, copias
        )
    }

    private fun Libro.toEntity() = LibroEntity(
        id = id,
        isbn = isbn,
        titulo = titulo,
        subtitulo = subtitulo,
        editorial = editorial,
        edicion = edicion,
        anioPub = anioPub,
        numPaginas = numPaginas,
        categoria = categoria,
        idioma = idioma,
        descripcion = descripcion,
        portadaUrl = portadaUrl,
        autoresStr = autores.joinToString(";") { it.autor.nombre },
        totalCopias = copias.size,
        copiasDisponibles = disponibles(),
        pasillo = copias.firstOrNull()?.pasillo,
        estante = copias.firstOrNull()?.estante
    )

    // ─── Dataset de respaldo (sin red y BD vacía) ───────────────────────────

    private fun librosFallback(): List<LibroEntity> = listOf(
        LibroEntity(1, "978-6075228280", "Cálculo Tomo I", "Trascendentes tempranas",
            "CENGAGE", "10a", 2019, 658, "Matemáticas", "Español",
            "Cálculo de una variable con enfoque en trascendentes tempranas.",
            "https://m.media-amazon.com/images/I/71HU6XkiZ5L._AC_UF1000,1000_QL80_.jpg",
            "Larson, Ron", 3, 2, "A", "3"),
        LibroEntity(2, "978-6075228297", "Cálculo Tomo II", "Trascendentes tempranas",
            "CENGAGE", "10a", 2019, 720, "Matemáticas", "Español",
            "Continuación del cálculo de una variable.",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRelM6LMu35axJE1r0EdzImysxE1f2nxGXXoA&s",
            "Larson, Ron", 2, 1, "A", "3"),
        LibroEntity(3, "978-9702610540", "Álgebra Lineal", null,
            "Prentice Hall", "8a", 2013, 559, "Matemáticas", "Español",
            "Introducción al álgebra lineal con aplicaciones.",
            "https://m.media-amazon.com/images/I/61jm7YLfqbL._AC_UF1000,1000_QL80_.jpg",
            "Lay, David C.", 1, 1, "A", "4"),
        LibroEntity(4, "978-6073238298", "Histología", "Texto y atlas con biología celular",
            "Wolters Kluwer", "7a", 2016, 930, "Medicina", "Español",
            "Atlas completo de histología con correlaciones clínicas.",
            "https://m.media-amazon.com/images/I/511Dg1gyX+L._SX342_SY445_ML2_.jpg",
            "Ross, Michael H.; Pawlina, Wojciech", 3, 2, "C", "1"),
        LibroEntity(5, "978-6071512684", "Principios de Economía", null,
            "CENGAGE", "7a", 2015, 848, "Economía", "Español",
            "Introducción a los principios micro y macroeconómicos.", null,
            "Mankiw, N. Gregory", 2, 0, "B", "2"),
        LibroEntity(6, "978-0132350884", "Clean Code", "A Handbook of Agile Software Craftsmanship",
            "Prentice Hall", "1a", 2008, 431, "Tecnología", "Inglés",
            "Guía práctica para escribir código limpio y mantenible.", null,
            "Martin, Robert C.", 2, 2, "D", "5"),
        LibroEntity(7, "978-9681653149", "Cien años de soledad", null,
            "Real Academia Española", "Edición conmemorativa", 2007, 496, "Literatura", "Español",
            "La obra maestra del realismo mágico latinoamericano.", null,
            "García Márquez, Gabriel", 1, 1, "E", "1"),
        LibroEntity(8, "978-6074383843", "Derecho Civil", "Parte General",
            "Porrúa", "40a", 2021, 712, "Derecho", "Español",
            "Texto clásico del derecho civil mexicano.", null,
            "Rojina Villegas, Rafael", 2, 1, "F", "2")
    )
}
