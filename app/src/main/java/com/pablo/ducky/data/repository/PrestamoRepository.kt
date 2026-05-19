package com.pablo.ducky.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.pablo.ducky.data.local.AppDatabase
import com.pablo.ducky.data.local.MultaEntity
import com.pablo.ducky.data.local.PrestamoEntity
import com.pablo.ducky.data.local.SessionManager
import com.pablo.ducky.data.model.Libro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class PrestamoRepository(context: Context) {

    private val prestamoDao = AppDatabase.getInstance(context).prestamoDao()
    private val multaDao    = AppDatabase.getInstance(context).multaDao()
    private val session     = SessionManager(context)

    private val TARIFA_DIARIA = 5.0   // $5 MXN por día de retraso
    private val DIAS_PRESTAMO  = 8L   // duración estándar del préstamo

    /** LiveData de préstamos activos del usuario logueado. */
    fun obtenerPrestamosActivos(): LiveData<List<PrestamoEntity>> {
        val uid = session.obtenerUsuarioId()
        return prestamoDao.obtenerActivosPorUsuario(uid)
    }

    /** LiveData de multas del usuario logueado. */
    fun obtenerMultas(): LiveData<List<MultaEntity>> {
        val uid = session.obtenerUsuarioId()
        return multaDao.obtenerPorUsuario(uid)
    }

    /** LiveData del total de deuda del usuario logueado. */
    fun totalDeuda(): LiveData<Double> {
        val uid = session.obtenerUsuarioId()
        return multaDao.totalDeuda(uid)
    }

    /**
     * Crea un nuevo préstamo para el libro indicado.
     * Automáticamente reduce una copia disponible (lógico, en Room).
     */
    suspend fun solicitarPrestamo(libro: Libro): Result<PrestamoEntity> =
        withContext(Dispatchers.IO) {
            val uid  = session.obtenerUsuarioId()
            val now  = System.currentTimeMillis()
            val venc = now + TimeUnit.DAYS.toMillis(DIAS_PRESTAMO)

            val prestamo = PrestamoEntity(
                usuarioId       = uid,
                libroId         = libro.id,
                tituloLibro     = libro.titulo,
                autorLibro      = libro.autoresString(),
                portadaUrl      = libro.portadaUrl,
                fechaSolicitud  = now,
                fechaVencimiento = venc
            )
            val id = prestamoDao.insertar(prestamo)
            Result.success(prestamo.copy(id = id.toInt()))
        }

    /**
     * Revisa todos los préstamos vencidos del usuario y genera multas
     * por los que aún no tienen multa asignada.
     * Se llama al abrir la pantalla de préstamos o multas.
     */
    suspend fun generarMultasPendientes() = withContext(Dispatchers.IO) {
        val uid      = session.obtenerUsuarioId()
        val ahora    = System.currentTimeMillis()
        val vencidos = prestamoDao.obtenerVencidosSinMulta(uid, ahora)

        vencidos.forEach { prestamo ->
            val diasRetraso = TimeUnit.MILLISECONDS.toDays(ahora - prestamo.fechaVencimiento)
                .coerceAtLeast(1).toInt()
            val monto = diasRetraso * TARIFA_DIARIA

            multaDao.insertar(
                MultaEntity(
                    usuarioId    = uid,
                    prestamoId   = prestamo.id,
                    tituloLibro  = prestamo.tituloLibro,
                    fechaGenerada = ahora,
                    diasRetraso  = diasRetraso,
                    monto        = monto
                )
            )
        }
    }
}
