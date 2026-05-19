package com.pablo.ducky.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PrestamoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(prestamo: PrestamoEntity): Long

    /** Préstamos activos del usuario — observable con LiveData. */
    @Query("SELECT * FROM prestamos WHERE usuarioId = :usuarioId AND activo = 1 ORDER BY fechaVencimiento ASC")
    fun obtenerActivosPorUsuario(usuarioId: Int): LiveData<List<PrestamoEntity>>

    /** Préstamos vencidos sin multa generada aún (para el job de multas). */
    @Query("""
        SELECT p.* FROM prestamos p
        WHERE p.usuarioId = :usuarioId
          AND p.activo = 1
          AND p.fechaVencimiento < :ahora
          AND p.id NOT IN (SELECT prestamoId FROM multas WHERE usuarioId = :usuarioId)
    """)
    suspend fun obtenerVencidosSinMulta(usuarioId: Int, ahora: Long): List<PrestamoEntity>

    /** Marca un préstamo como devuelto. */
    @Query("UPDATE prestamos SET activo = 0 WHERE id = :id")
    suspend fun marcarDevuelto(id: Int)
}
