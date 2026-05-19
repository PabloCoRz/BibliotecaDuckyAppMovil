package com.pablo.ducky.ui.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.pablo.ducky.R
import com.pablo.ducky.databinding.FragmentLandingBinding

/**
 * Pantalla principal (home) de la biblioteca.
 * Muestra un buscador y los libros destacados cargados desde Room vía [LandingViewModel].
 */
class LandingFragment : Fragment() {

    private var _binding: FragmentLandingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LandingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botón buscar → navega a SearchFragment con el query
        binding.btnBuscar.setOnClickListener {
            val query = binding.etSearchLanding.text.toString().trim()
            val action = LandingFragmentDirections.actionLandingToSearch(query)
            findNavController().navigate(action)
        }

        // También busca al presionar Enter en el campo
        binding.etSearchLanding.setOnEditorActionListener { _, _, _ ->
            binding.btnBuscar.performClick()
            true
        }

        // Observa los libros destacados desde Room
        viewModel.librosDestacados.observe(viewLifecycleOwner) { libros ->
            binding.layoutLibrosDestacados.removeAllViews()
            libros.take(4).forEach { libro ->
                val item = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_libro_destacado, binding.layoutLibrosDestacados, false)

                item.findViewById<TextView>(R.id.txtTituloDestacado).text = libro.titulo
                item.findViewById<TextView>(R.id.txtAutorDestacado).text = libro.autoresString()

                Glide.with(this)
                    .load(libro.portadaUrl)
                    .placeholder(R.drawable.bg_portada_placeholder)
                    .into(item.findViewById<ImageView>(R.id.imgPortadaDestacado))

                item.setOnClickListener {
                    val action = LandingFragmentDirections.actionLandingToDetalle(libro.id)
                    findNavController().navigate(action)
                }

                binding.layoutLibrosDestacados.addView(item)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
