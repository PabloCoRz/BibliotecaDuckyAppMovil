package com.pablo.ducky.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pablo.ducky.R
import com.pablo.ducky.databinding.FragmentLoginBinding

/**
 * Pantalla de inicio de sesión.
 * Valida que email y contraseña no estén vacíos antes de navegar.
 */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
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
                    binding.etPassword.error = "Ingresa tu contraseña"
                    binding.etPassword.requestFocus()
                }
                password.length < 6 -> {
                    binding.etPassword.error = "Mínimo 6 caracteres"
                    binding.etPassword.requestFocus()
                }
                else -> {
                    findNavController().navigate(R.id.action_login_to_landing)
                }
            }
        }

        binding.btnGoSignup.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_signup)
        }

        val tvForgot = binding.tvForgotPassword
        tvForgot.paintFlags = tvForgot.paintFlags or android.graphics.Paint.UNDERLINE_TEXT_FLAG
        tvForgot.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_forgot)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
