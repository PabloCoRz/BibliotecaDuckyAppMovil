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
import com.pablo.ducky.data.local.SessionManager
import com.pablo.ducky.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoginViewModel.State.Success -> {
                    SessionManager(requireContext()).saveSession(state.email)
                    findNavController().navigate(R.id.action_login_to_landing)
                }
                is LoginViewModel.State.Error -> {
                    Toast.makeText(requireContext(), state.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                binding.etEmail.text.toString().trim(),
                binding.etPassword.text.toString()
            )
        }

        binding.btnGoSignup.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_signup)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
