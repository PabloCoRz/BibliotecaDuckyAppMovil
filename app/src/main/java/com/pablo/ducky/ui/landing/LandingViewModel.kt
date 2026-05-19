package com.pablo.ducky.ui.landing

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pablo.ducky.data.model.Libro
import com.pablo.ducky.data.repository.LibroRepository
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de inicio (Landing).
 * Obtiene y expone los libros destacados y maneja la sincronización inicial con la API.
 */
class LandingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LibroRepository(application)

    /** Lista de libros destacados (primeros 4 de la BD). */
    val librosDestacados: LiveData<List<Libro>> = repository.obtenerTodosLive()

    private val _sincronizando = MutableLiveData(false)
    val sincronizando: LiveData<Boolean> = _sincronizando

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    init {
        sincronizar()
    }

    /** Intenta sincronizar con la API. Si falla, Room ya tiene datos del caché. */
    fun sincronizar() {
        viewModelScope.launch {
            _sincronizando.value = true
            repository.sincronizar()
            _sincronizando.value = false
        }
    }
}
