package com.pablo.ducky.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pablo.ducky.DuckyApp
import com.pablo.ducky.data.local.entity.PrestamoEntity
import com.pablo.ducky.data.model.Libro
import com.pablo.ducky.data.repository.LibroRepository
import kotlinx.coroutines.launch

class DetalleViewModel(app: Application) : AndroidViewModel(app) {

    private val dao by lazy { (getApplication<DuckyApp>()).database.prestamoDao() }

    private val _libro = MutableLiveData<Libro?>()
    val libro: LiveData<Libro?> = _libro

    private val _prestamoGuardado = MutableLiveData<Boolean>()
    val prestamoGuardado: LiveData<Boolean> = _prestamoGuardado

    fun cargarLibro(libroId: Int) {
        viewModelScope.launch {
            val resultado = LibroRepository().buscar("").getOrNull()?.find { it.id == libroId }
            _libro.value = resultado
        }
    }

    fun guardarPrestamo(prestamo: PrestamoEntity) {
        viewModelScope.launch {
            dao.insert(prestamo)
            _prestamoGuardado.value = true
        }
    }

    fun resetPrestamoGuardado() {
        _prestamoGuardado.value = false
    }
}
