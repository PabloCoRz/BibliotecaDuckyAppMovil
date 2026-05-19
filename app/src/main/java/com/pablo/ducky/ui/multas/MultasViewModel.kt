package com.pablo.ducky.ui.multas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pablo.ducky.data.repository.PrestamoRepository
import kotlinx.coroutines.launch

class MultasViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = PrestamoRepository(application)

    val multas    = repo.obtenerMultas()
    val totalDeuda = repo.totalDeuda()

    init {
        viewModelScope.launch { repo.generarMultasPendientes() }
    }
}
