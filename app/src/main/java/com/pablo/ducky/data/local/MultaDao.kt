package com.pablo.ducky.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MultaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(multa: MultaEntity)

    /** Multas del usuario — observable con LiveData. */
    @Query("SELECT * FROM multas WHERE usuarioId = :usuarioId ORDER BY fechaGenerada DESC")
    fun obtenerPorUsuario(usuarioId: Int): LiveData<List<MultaEntity>>

    /** Total de deuda pendiente del usuario. */
    @Query("SELECT COALESCE(SUM(monto), 0) FROM multas WHERE usuarioId = :usuarioId AND pagada = 0")
    fun totalDeuda(usuarioId: Int): LiveData<Double>

    /** Marca una multa como pagada. */
    @Query("UPDATE multas SET pagada = 1 WHERE id = :id")
    suspend fun marcarPagada(id: Int)
}
