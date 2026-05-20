package com.pablo.ducky.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pablo.ducky.DuckyApp
import com.pablo.ducky.data.local.entity.UsuarioEntity
import kotlinx.coroutines.launch

class SignupViewModel(app: Application) : AndroidViewModel(app) {

    private val dao by lazy { (getApplication<DuckyApp>()).database.usuarioDao() }

    sealed class State {
        object Success : State()
        data class Error(val msg: String) : State()
    }

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    fun register(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = State.Error("Ingresa email y contrasena")
            return
        }
        viewModelScope.launch {
            val existing = dao.findByEmail(email)
            _state.value = if (existing != null) State.Error("Este email ya tiene una cuenta")
                           else {
                               dao.insert(UsuarioEntity(email = email, password = password))
                               State.Success
                           }
        }
    }
}
