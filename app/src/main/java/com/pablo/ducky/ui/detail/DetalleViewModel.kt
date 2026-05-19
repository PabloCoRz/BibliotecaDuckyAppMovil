package com.pablo.ducky.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pablo.ducky.data.model.Libro
import com.pablo.ducky.data.repository.LibroRepository
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de detalle de un libro.
 * Carga el libro desde Room por su ID y lo expone como LiveData.
 */
class DetalleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LibroRepository(application)

    private val _libro = MutableLiveData<Libro?>()
    val libro: LiveData<Libro?> = _libro

    private val _cargando = MutableLiveData(false)
    val cargando: LiveData<Boolean> = _cargando

    /**
     * Carga el libro con el [libroId] indicado desde la base de datos local.
     * Operación asíncrona con coroutine — no bloquea el hilo principal.
     */
    fun cargarLibro(libroId: Int) {
        viewModelScope.launch {
            _cargando.value = true
            _libro.value = repository.obtenerPorId(libroId)
            _cargando.value = false
        }
    }
}
