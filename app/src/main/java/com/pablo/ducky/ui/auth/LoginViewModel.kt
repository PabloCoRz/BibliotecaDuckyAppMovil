package com.pablo.ducky.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pablo.ducky.DuckyApp
import kotlinx.coroutines.launch

class LoginViewModel(app: Application) : AndroidViewModel(app) {

    private val dao by lazy { (getApplication<DuckyApp>()).database.usuarioDao() }

    sealed class State {
        data class Success(val email: String) : State()
        data class Error(val msg: String) : State()
    }

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = State.Error("Ingresa email y contrasena")
            return
        }
        viewModelScope.launch {
            val user = dao.login(email, password)
            _state.value = if (user != null) State.Success(email)
                           else State.Error("Cuenta no encontrada o contrasena incorrecta")
        }
    }
}
