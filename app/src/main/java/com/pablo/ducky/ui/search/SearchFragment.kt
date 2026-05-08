package com.pablo.ducky.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pablo.ducky.R

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: LibroAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // declare all views first
        val etSearch      = view.findViewById<EditText>(R.id.etSearch)
        val recycler      = view.findViewById<RecyclerView>(R.id.recyclerLibros)
        val progressBar   = view.findViewById<ProgressBar>(R.id.progressBar)
        val layoutEmpty   = view.findViewById<LinearLayout>(R.id.layoutEmpty)
        val txtResultados = view.findViewById<TextView>(R.id.txtResultados)
        val txtEmpty      = view.findViewById<TextView>(R.id.txtEmptyMessage)

        // now safe to use etSearch
        val query = arguments?.getString("query") ?: ""
        if (query.isNotBlank()) {
            etSearch.setText(query)
            viewModel.buscar(query)
        }

        adapter = LibroAdapter { libro ->
            val action = SearchFragmentDirections.actionSearchToDetalle(libro.id)
            findNavController().navigate(action)
        }

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        viewModel.libros.observe(viewLifecycleOwner) { libros ->
            adapter.submitList(libros)
            val currentQuery = etSearch.text.toString()
            when {
                currentQuery.isBlank() -> {
                    layoutEmpty.visibility  = View.VISIBLE
                    recycler.visibility     = View.GONE
                    txtResultados.text      = ""
                    txtEmpty.text           = "Busca un libro en nuestra biblioteca"
                }
                libros.isEmpty() -> {
                    layoutEmpty.visibility  = View.VISIBLE
                    recycler.visibility     = View.GONE
                    txtResultados.text      = ""
                    txtEmpty.text           = "No se encontraron resultados para \"$currentQuery\""
                }
                else -> {
                    layoutEmpty.visibility  = View.GONE
                    recycler.visibility     = View.VISIBLE
                    txtResultados.text      = "${libros.size} resultado(s) para \"$currentQuery\""
                }
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.buscar(s?.toString() ?: "")
            }
        })
    }
}