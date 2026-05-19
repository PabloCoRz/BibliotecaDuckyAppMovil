package com.pablo.ducky.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.pablo.ducky.data.model.Libro
import com.pablo.ducky.data.repository.LibroRepository

/**
 * ViewModel para la pantalla de búsqueda.
 *
 * Usa LiveData de Room a través del repositorio: cada vez que cambia el query,
 * [buscarLibrosLive] redirige a la consulta SQL de Room y la UI se actualiza
 * reactivamente sin lógica adicional.
 */
class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LibroRepository(application)

    private val _query = MutableLiveData("")

    /** Resultados de búsqueda observables, enlazados directamente a Room. */
    val libros: LiveData<List<Libro>> = _query.switchMap { q ->
        if (q.isBlank()) MutableLiveData(emptyList())
        else repository.buscarLibrosLive(q)
    }

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    /**
     * Actualiza el query de búsqueda.
     * El cambio se propaga automáticamente a [libros] vía switchMap + Room.
     */
    fun buscar(query: String) {
        _query.value = query
    }
}
