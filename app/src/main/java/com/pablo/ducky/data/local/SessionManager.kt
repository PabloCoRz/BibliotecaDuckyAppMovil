package com.pablo.ducky.data.local

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("ducky_session", Context.MODE_PRIVATE)
    fun saveSession(email: String) = prefs.edit().putString("email", email).apply()
    fun getEmail(): String? = prefs.getString("email", null)
    fun clearSession() = prefs.edit().clear().apply()
}
