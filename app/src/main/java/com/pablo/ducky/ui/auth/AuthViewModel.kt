package com.pablo.ducky.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pablo.ducky.data.repository.UsuarioRepository
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = UsuarioRepository(application)

    private val _loginResult = MutableLiveData<Result<Unit>?>()
    val loginResult: LiveData<Result<Unit>?> = _loginResult

    private val _registroResult = MutableLiveData<Result<Unit>?>()
    val registroResult: LiveData<Result<Unit>?> = _registroResult

    private val _cargando = MutableLiveData(false)
    val cargando: LiveData<Boolean> = _cargando

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _cargando.value = true
            val result = repo.login(email, password)
            _loginResult.value = result.map { }
            _cargando.value = false
        }
    }

    fun registrar(nombre: String, email: String, password: String) {
        viewModelScope.launch {
            _cargando.value = true
            val result = repo.registrar(nombre, email, password)
            _registroResult.value = result.map { }
            _cargando.value = false
        }
    }

    fun resetLoginResult()    { _loginResult.value = null }
    fun resetRegistroResult() { _registroResult.value = null }

    fun obtenerSesion() = repo.obtenerSesion()
}
