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

        viewModel.prestamos.observe(viewLifecycleOwner) { lista ->
            adapter.submitList(lista)
            if (lista.isEmpty()) {
                binding.recyclerPrestamos.visibility = View.GONE
                binding.layoutEmpty.visibility       = View.VISIBLE
            } else {
                binding.recyclerPrestamos.visibility = View.VISIBLE
                binding.layoutEmpty.visibility       = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
