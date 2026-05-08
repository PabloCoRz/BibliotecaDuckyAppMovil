package com.pablo.ducky.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pablo.ducky.data.model.Libro
import com.pablo.ducky.data.repository.LibroRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repo = LibroRepository()

    private val _libros = MutableLiveData<List<Libro>>(emptyList())
    val libros: LiveData<List<Libro>> = _libros

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private var searchJob: Job? = null

    fun buscar(query: String) {
        searchJob?.cancel()
        if (query.isBlank()) {
            _libros.value = emptyList()
            return
        }
        searchJob = viewModelScope.launch {
            delay(400)
            _loading.value = true
            _error.value = null
            repo.buscar(query).fold(
                onSuccess = { _libros.value = it },
                onFailure = { _error.value = "Error al conectar con el servidor" }
            )
            _loading.value = false
        }
    }
}