package com.pablo.ducky.ui.multas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pablo.ducky.DuckyApp
import com.pablo.ducky.data.local.entity.PrestamoEntity
import kotlinx.coroutines.launch

class MultasViewModel(app: Application) : AndroidViewModel(app) {

    private val dao by lazy { (getApplication<DuckyApp>()).database.prestamoDao() }

    private val _multas = MutableLiveData<List<PrestamoEntity>>()
    val multas: LiveData<List<PrestamoEntity>> = _multas

    fun cargar() {
        viewModelScope.launch {
            val ahora = System.currentTimeMillis()
            _multas.value = dao.getAll().filter { !it.devuelto && ahora > it.fechaDevolucion }
        }
    }
}
