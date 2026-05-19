package com.pablo.ducky.data.repository

import android.content.Context
import com.pablo.ducky.data.local.AppDatabase
import com.pablo.ducky.data.local.SessionManager
import com.pablo.ducky.data.local.UsuarioEntity
import com.pablo.ducky.util.HashUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsuarioRepository(context: Context) {

    private val dao     = AppDatabase.getInstance(context).usuarioDao()
    private val session = SessionManager(context)

    /**
     * Registra un usuario nuevo.
     * @return Result.success con el id, o Result.failure si el email ya existe.
     */
    suspend fun registrar(nombre: String, email: String, password: String): Result<Int> =
        withContext(Dispatchers.IO) {
            try {
                val yaExiste = dao.existeEmail(email.lowercase()) > 0
                if (yaExiste) return@withContext Result.failure(Exception("El correo ya está registrado"))

                val hash = HashUtils.sha256(password)
                val id = dao.registrar(
                    UsuarioEntity(nombre = nombre, email = email.lowercase(), passwordHash = hash)
                ).toInt()
                Result.success(id)
            } catch (e: Exception) {
                Result.failure(Exception("El correo ya está registrado"))
            }
        }

    /**
     * Inicia sesión.
     * @return Result.success con UsuarioEntity, o Result.failure con mensaje de error.
     */
    suspend fun login(email: String, password: String): Result<UsuarioEntity> =
        withContext(Dispatchers.IO) {
            val hash    = HashUtils.sha256(password)
            val usuario = dao.login(email.lowercase(), hash)
            if (usuario != null) {
                session.guardarSesion(usuario.id, usuario.email, usuario.nombre)
                Result.success(usuario)
            } else {
                // Distingue entre "no existe" y "contraseña incorrecta"
                val existe = dao.existeEmail(email.lowercase()) > 0
                if (!existe)
                    Result.failure(Exception("No existe una cuenta con ese correo"))
                else
                    Result.failure(Exception("Contraseña incorrecta"))
            }
        }

    fun obtenerSesion() = session
}
