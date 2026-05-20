package com.pablo.ducky.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pablo.ducky.R
import com.pablo.ducky.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SignupViewModel.State.Success -> {
                    Toast.makeText(requireContext(), "Cuenta creada. Inicia sesion", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_signup_to_login)
                }
                is SignupViewModel.State.Error -> {
                    Toast.makeText(requireContext(), state.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnSignup.setOnClickListener {
            viewModel.register(
                binding.etEmail.text.toString().trim(),
                binding.etPassword.text.toString()
            )
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
