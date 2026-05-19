package com.pablo.ducky.ui.prestamos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pablo.ducky.data.repository.PrestamoRepository
import kotlinx.coroutines.launch

class PrestamosViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = PrestamoRepository(application)

    val prestamos = repo.obtenerPrestamosActivos()

    init {
        // Genera multas de préstamos vencidos al abrir la pantalla
        viewModelScope.launch { repo.generarMultasPendientes() }
    }
}
