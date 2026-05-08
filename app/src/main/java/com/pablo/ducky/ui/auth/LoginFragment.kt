package com.pablo.ducky.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pablo.ducky.R

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.btnLogin).setOnClickListener {
            findNavController().navigate(R.id.action_login_to_landing)
        }

        view.findViewById<TextView>(R.id.btnGoSignup).setOnClickListener {
            findNavController().navigate(R.id.action_login_to_signup)
        }

        // underline + click for forgot password
        val tvForgot = view.findViewById<TextView>(R.id.tvForgotPassword)
        tvForgot.paintFlags = tvForgot.paintFlags or android.graphics.Paint.UNDERLINE_TEXT_FLAG
        tvForgot.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_forgot)
        }
    }
}