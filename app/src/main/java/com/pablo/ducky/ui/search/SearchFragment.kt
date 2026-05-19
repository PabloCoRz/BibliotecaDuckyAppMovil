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
import com.pablo.ducky.databinding.FragmentSearchBinding

/**
 * Pantalla de búsqueda de libros.
 * Observa [SearchViewModel.libros] (LiveData de Room) y actualiza la lista reactivamente.
 */
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

        adapter = LibroAdapter { libro ->
            val action = SearchFragmentDirections.actionSearchToDetalle(libro.id)
            findNavController().navigate(action)
        }

        binding.recyclerLibros.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerLibros.adapter = adapter

        // Si llegamos desde Landing con un query inicial, lo aplica
        val queryInicial = arguments?.getString("query") ?: ""
        if (queryInicial.isNotBlank()) {
            binding.etSearch.setText(queryInicial)
            viewModel.buscar(queryInicial)
        }

        // Observa resultados de Room
        viewModel.libros.observe(viewLifecycleOwner) { libros ->
            adapter.submitList(libros)
            val currentQuery = binding.etSearch.text.toString()
            when {
                currentQuery.isBlank() -> mostrarEstadoVacio("Busca un libro en nuestra biblioteca")
                libros.isEmpty() -> mostrarEstadoVacio("No se encontraron resultados para \"$currentQuery\"")
                else -> {
                    binding.layoutEmpty.visibility  = View.GONE
                    binding.recyclerLibros.visibility = View.VISIBLE
                    binding.txtResultados.text = "${libros.size} resultado(s) para \"$currentQuery\""
                }
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        // Búsqueda en tiempo real al escribir
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.buscar(s?.toString() ?: "")
            }
        })
    }

    private fun mostrarEstadoVacio(mensaje: String) {
        binding.layoutEmpty.visibility   = View.VISIBLE
        binding.recyclerLibros.visibility = View.GONE
        binding.txtResultados.text        = ""
        binding.txtEmptyMessage.text      = mensaje
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
