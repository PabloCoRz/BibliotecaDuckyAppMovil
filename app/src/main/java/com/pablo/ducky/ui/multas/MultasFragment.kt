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
import java.util.concurrent.TimeUnit

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

        viewModel.multas.observe(viewLifecycleOwner) { multas ->
            adapter.submitList(multas)
            if (multas.isEmpty()) {
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.recyclerMultas.visibility = View.GONE
                binding.txtTotalMultas.visibility = View.GONE
            } else {
                binding.layoutEmpty.visibility = View.GONE
                binding.recyclerMultas.visibility = View.VISIBLE
                val ahora = System.currentTimeMillis()
                val totalDias = multas.sumOf {
                    TimeUnit.MILLISECONDS.toDays(ahora - it.fechaDevolucion).toInt()
                }
                binding.txtTotalMultas.text = "Total adeudado: $${"%.2f".format(totalDias * 5.0)} MXN"
                binding.txtTotalMultas.visibility = View.VISIBLE
            }
        }

        viewModel.cargar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
