package com.pablo.ducky.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pablo.ducky.R
import com.pablo.ducky.databinding.FragmentSignupBinding

/**
 * Pantalla de registro de usuario.
 * Valida email y contraseña antes de completar el registro simulado.
 */
class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignup.setOnClickListener {
            val email    = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()

            when {
                email.isEmpty() -> {
                    binding.etEmail.error = "Ingresa tu correo"
                    binding.etEmail.requestFocus()
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.etEmail.error = "Correo inválido"
                    binding.etEmail.requestFocus()
                }
                password.isEmpty() -> {
                    binding.etPassword.error = "Ingresa una contraseña"
                    binding.etPassword.requestFocus()
                }
                password.length < 6 -> {
                    binding.etPassword.error = "Mínimo 6 caracteres"
                    binding.etPassword.requestFocus()
                }
                else -> {
                    // Registro exitoso → regresa al login
                    findNavController().navigate(R.id.action_signup_to_login)
                }
            }
        }

        binding.btnGoLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signup_to_login)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
