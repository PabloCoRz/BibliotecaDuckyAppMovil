package com.pablo.ducky.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * DAO (Data Access Object) para operaciones de base de datos de libros.
 * Provee acceso reactivo vía LiveData y operaciones suspend para coroutines.
 */
@Dao
interface LibroDao {

    /** Busca libros cuyo título, autor, ISBN o categoría coincidan con [query]. */
    @Query("""
        SELECT * FROM libros
        WHERE titulo LIKE '%' || :query || '%'
           OR autoresStr LIKE '%' || :query || '%'
           OR isbn LIKE '%' || :query || '%'
           OR categoria LIKE '%' || :query || '%'
           OR editorial LIKE '%' || :query || '%'
        ORDER BY titulo ASC
    """)
    fun buscarLibros(query: String): LiveData<List<LibroEntity>>

    /** Retorna todos los libros para la pantalla de landing (destacados). */
    @Query("SELECT * FROM libros ORDER BY titulo ASC")
    fun obtenerTodos(): LiveData<List<LibroEntity>>

    /** Obtiene un libro específico por su ID. */
    @Query("SELECT * FROM libros WHERE id = :id")
    suspend fun obtenerPorId(id: Int): LibroEntity?

    /** Inserta o actualiza la lista completa de libros desde la fuente remota. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(libros: List<LibroEntity>)

    /** Cuenta los libros almacenados (útil para saber si la BD está vacía). */
    @Query("SELECT COUNT(*) FROM libros")
    suspend fun contarLibros(): Int
}
