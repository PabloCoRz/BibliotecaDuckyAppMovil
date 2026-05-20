package com.pablo.ducky.ui.landing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pablo.ducky.data.model.Libro
import com.pablo.ducky.data.repository.LibroRepository
import kotlinx.coroutines.launch

class LandingViewModel : ViewModel() {

    private val _libros = MutableLiveData<List<Libro>>()
    val libros: LiveData<List<Libro>> = _libros

    init {
        viewModelScope.launch {
            val result = LibroRepository().buscar("").getOrNull() ?: emptyList()
            _libros.value = result.take(4)
        }
    }
}
