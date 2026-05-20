package com.pablo.ducky.ui.prestamos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pablo.ducky.DuckyApp
import com.pablo.ducky.data.local.entity.PrestamoEntity
import kotlinx.coroutines.launch

class PrestamosViewModel(app: Application) : AndroidViewModel(app) {

    private val dao by lazy { (getApplication<DuckyApp>()).database.prestamoDao() }

    private val _prestamos = MutableLiveData<List<PrestamoEntity>>()
    val prestamos: LiveData<List<PrestamoEntity>> = _prestamos

    fun cargar() {
        viewModelScope.launch {
            _prestamos.value = dao.getAll()
        }
    }
}
