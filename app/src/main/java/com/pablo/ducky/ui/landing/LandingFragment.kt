package com.pablo.ducky.ui.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.pablo.ducky.MainActivity
import com.pablo.ducky.R
import com.pablo.ducky.databinding.FragmentLandingBinding
import com.pablo.ducky.databinding.ItemLibroDestacadoBinding

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

        binding.btnMenu.setOnClickListener {
            (activity as MainActivity).openDrawer()
        }

        binding.btnBuscar.setOnClickListener {
            val query = binding.etSearchLanding.text.toString()
            val action = LandingFragmentDirections.actionLandingToSearch(query)
            findNavController().navigate(action)
        }

        viewModel.libros.observe(viewLifecycleOwner) { libros ->
            binding.layoutLibrosDestacados.removeAllViews()
            libros.forEach { libro ->
                val item = ItemLibroDestacadoBinding.inflate(
                    layoutInflater, binding.layoutLibrosDestacados, false
                )
                item.txtTituloDestacado.text = libro.titulo
                item.txtAutorDestacado.text = libro.autoresString()

                Glide.with(this)
                    .load(libro.portadaUrl)
                    .placeholder(R.drawable.bg_portada_placeholder)
                    .into(item.imgPortadaDestacado)

                item.root.setOnClickListener {
                    val action = LandingFragmentDirections.actionLandingToDetalle(libro.id)
                    findNavController().navigate(action)
                }

                binding.layoutLibrosDestacados.addView(item.root)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
