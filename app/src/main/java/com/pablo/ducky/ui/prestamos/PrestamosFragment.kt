package com.pablo.ducky.ui.prestamos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pablo.ducky.databinding.FragmentPrestamosBinding

class PrestamosFragment : Fragment() {

    private var _binding: FragmentPrestamosBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PrestamosViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrestamosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        val adapter = PrestamoAdapter()
        binding.recyclerPrestamos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerPrestamos.adapter = adapter

        viewModel.prestamos.observe(viewLifecycleOwner) { prestamos ->
            adapter.submitList(prestamos)
            if (prestamos.isEmpty()) {
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.recyclerPrestamos.visibility = View.GONE
            } else {
                binding.layoutEmpty.visibility = View.GONE
                binding.recyclerPrestamos.visibility = View.VISIBLE
            }
        }

        viewModel.cargar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
