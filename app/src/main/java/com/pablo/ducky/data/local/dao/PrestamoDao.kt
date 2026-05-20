package com.pablo.ducky.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.pablo.ducky.data.local.entity.PrestamoEntity

@Dao
interface PrestamoDao {
    @Insert
    suspend fun insert(prestamo: PrestamoEntity)

    @Query("SELECT * FROM prestamos ORDER BY fechaSolicitud DESC")
    suspend fun getAll(): List<PrestamoEntity>
}
