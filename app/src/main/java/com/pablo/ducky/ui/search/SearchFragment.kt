package com.pablo.ducky.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pablo.ducky.MainActivity
import com.pablo.ducky.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: LibroAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnMenu.setOnClickListener { (activity as MainActivity).openDrawer() }

        val query = arguments?.getString("query") ?: ""
        if (query.isNotBlank()) {
            binding.etSearch.setText(query)
            viewModel.buscar(query)
        }

        adapter = LibroAdapter(
            onClick = { libro ->
                val action = SearchFragmentDirections.actionSearchToDetalle(libro.id)
                findNavController().navigate(action)
            },
            onReserva = { libro -> viewModel.guardarPrestamoRapido(libro) }
        )

        binding.recyclerLibros.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerLibros.adapter = adapter

        viewModel.libros.observe(viewLifecycleOwner) { libros ->
            adapter.submitList(libros)
            val currentQuery = binding.etSearch.text.toString()
            when {
                currentQuery.isBlank() -> {
                    binding.layoutEmpty.visibility = View.VISIBLE
                    binding.recyclerLibros.visibility = View.GONE
                    binding.txtResultados.text = ""
                    binding.txtEmptyMessage.text = "Busca un libro en nuestra biblioteca"
                }
                libros.isEmpty() -> {
                    binding.layoutEmpty.visibility = View.VISIBLE
                    binding.recyclerLibros.visibility = View.GONE
                    binding.txtResultados.text = ""
                    binding.txtEmptyMessage.text = "No se encontraron resultados para \"$currentQuery\""
                }
                else -> {
                    binding.layoutEmpty.visibility = View.GONE
                    binding.recyclerLibros.visibility = View.VISIBLE
                    binding.txtResultados.text = "${libros.size} resultado(s) para \"$currentQuery\""
                }
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.buscar(s?.toString() ?: "")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
