package com.pablo.ducky.ui.multas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pablo.ducky.databinding.FragmentMultasBinding

class MultasFragment : Fragment() {

    private var _binding: FragmentMultasBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MultasViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMultasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        val adapter = MultaAdapter()
        binding.recyclerMultas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMultas.adapter = adapter

        viewModel.multas.observe(viewLifecycleOwner) { lista ->
            adapter.submitList(lista)
            if (lista.isEmpty()) {
                binding.recyclerMultas.visibility = View.GONE
                binding.layoutEmpty.visibility    = View.VISIBLE
            } else {
                binding.recyclerMultas.visibility = View.VISIBLE
                binding.layoutEmpty.visibility    = View.GONE
            }
        }

        viewModel.totalDeuda.observe(viewLifecycleOwner) { total ->
            binding.txtTotalDeuda.text = "Total adeudado: $${String.format("%.2f", total)} MXN"
            binding.txtTotalDeuda.visibility = if (total > 0) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
