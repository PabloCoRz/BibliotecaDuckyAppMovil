package com.pablo.ducky

import android.app.Application
import com.pablo.ducky.data.local.AppDatabase

/**
 * Clase Application personalizada.
 * Inicializa la base de datos Room al arrancar el proceso.
 */
class DuckyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Fuerza la creación del singleton de la BD al inicio
        AppDatabase.getInstance(this)
    }
}
