package com.pablo.ducky.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pablo.ducky.databinding.FragmentForgotPasswordBinding

/**
 * Pantalla de recuperación de contraseña.
 * Valida que el email tenga formato válido antes de mostrar confirmación.
 */
class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEnviar.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            when {
                email.isEmpty() -> {
                    binding.etEmail.error = "Ingresa tu correo"
                    binding.etEmail.requestFocus()
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.etEmail.error = "Correo inválido"
                    binding.etEmail.requestFocus()
                }
                else -> {
                    Toast.makeText(
                        requireContext(),
                        "Correo de recuperación enviado a $email",
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().navigateUp()
                }
            }
        }

        binding.btnRegresar.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
