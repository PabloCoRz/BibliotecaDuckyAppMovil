package com.pablo.ducky.ui.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pablo.ducky.R
import com.pablo.ducky.data.repository.LibroRepository
import kotlinx.coroutines.runBlocking
import com.bumptech.glide.Glide
class LandingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_landing, container, false)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etSearch = view.findViewById<EditText>(R.id.etSearchLanding)

        view.findViewById<TextView>(R.id.btnBuscar).setOnClickListener {
            val query = etSearch.text.toString()
            val action = LandingFragmentDirections.actionLandingToSearch(query)
            findNavController().navigate(action)
        }

        // Load featured books (first 4)
        val libros = runBlocking {
            LibroRepository().buscar("").getOrNull() ?: emptyList()
        }.take(4)

        val container = view.findViewById<LinearLayout>(R.id.layoutLibrosDestacados)
        libros.forEach { libro ->
            val item = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_libro_destacado, container, false)

            item.findViewById<TextView>(R.id.txtTituloDestacado).text = libro.titulo
            item.findViewById<TextView>(R.id.txtAutorDestacado).text = libro.autoresString()

            // load cover image
            val imgPortada = item.findViewById<ImageView>(R.id.imgPortadaDestacado)
            Glide.with(this)
                .load(libro.portadaUrl)
                .placeholder(R.drawable.bg_portada_placeholder)
                .into(imgPortada)

            // tap goes directly to detail, not search
            item.setOnClickListener {
                val action = LandingFragmentDirections.actionLandingToDetalle(libro.id)
                findNavController().navigate(action)
            }

            container.addView(item)
        }
    }
}