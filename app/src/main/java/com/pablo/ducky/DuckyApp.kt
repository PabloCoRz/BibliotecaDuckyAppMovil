package com.pablo.ducky

import android.app.Application
import com.pablo.ducky.data.local.BibliotecaDatabase
import com.pablo.ducky.data.local.entity.PrestamoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DuckyApp : Application() {
    val database by lazy { BibliotecaDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            val dao = database.prestamoDao()
            if (dao.getAll().isEmpty()) {
                val ahora = System.currentTimeMillis()
                val dia = 24 * 60 * 60 * 1000L
                dao.insert(PrestamoEntity(
                    libroId = 1,
                    tituloLibro = "Don Quijote de la Mancha",
                    autorLibro = "Miguel de Cervantes",
                    portadaUrl = null,
                    fechaSolicitud = ahora - 45 * dia,
                    fechaDevolucion = ahora - 30 * dia,
                    devuelto = false
                ))
                dao.insert(PrestamoEntity(
                    libroId = 2,
                    tituloLibro = "Cien Años de Soledad",
                    autorLibro = "Gabriel García Márquez",
                    portadaUrl = null,
                    fechaSolicitud = ahora - 25 * dia,
                    fechaDevolucion = ahora - 15 * dia,
                    devuelto = false
                ))
                dao.insert(PrestamoEntity(
                    libroId = 3,
                    tituloLibro = "El Principito",
                    autorLibro = "Antoine de Saint-Exupéry",
                    portadaUrl = null,
                    fechaSolicitud = ahora - 12 * dia,
                    fechaDevolucion = ahora - 7 * dia,
                    devuelto = false
                ))
            }
        }
    }
}
