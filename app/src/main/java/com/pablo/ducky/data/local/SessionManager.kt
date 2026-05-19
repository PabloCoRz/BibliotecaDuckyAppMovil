package com.pablo.ducky.data.local

import android.content.Context

/**
 * Gestiona la sesión del usuario logueado usando SharedPreferences.
 * Permite saber qué usuario está activo sin volver a hacer login.
 */
class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("ducky_session", Context.MODE_PRIVATE)

    fun guardarSesion(usuarioId: Int, email: String, nombre: String) {
        prefs.edit()
            .putInt(KEY_ID, usuarioId)
            .putString(KEY_EMAIL, email)
            .putString(KEY_NOMBRE, nombre)
            .putBoolean(KEY_LOGUEADO, true)
            .apply()
    }

    fun cerrarSesion() {
        prefs.edit().clear().apply()
    }

    fun estaLogueado(): Boolean = prefs.getBoolean(KEY_LOGUEADO, false)

    fun obtenerUsuarioId(): Int = prefs.getInt(KEY_ID, -1)

    fun obtenerEmail(): String = prefs.getString(KEY_EMAIL, "") ?: ""

    fun obtenerNombre(): String = prefs.getString(KEY_NOMBRE, "") ?: ""

    companion object {
        private const val KEY_ID       = "usuario_id"
        private const val KEY_EMAIL    = "usuario_email"
        private const val KEY_NOMBRE   = "usuario_nombre"
        private const val KEY_LOGUEADO = "logueado"
    }
}
