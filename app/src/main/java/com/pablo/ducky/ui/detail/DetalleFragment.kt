package com.pablo.ducky.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.pablo.ducky.R
import com.pablo.ducky.databinding.FragmentDetalleBinding

/**
 * Pantalla de detalle de un libro.
 * Recibe el [libroId] por Safe Args, lo carga con [DetalleViewModel] desde Room,
 * y muestra toda la información del libro sin bloquear el hilo principal.
 */
class DetalleFragment : Fragment() {

    private var _binding: FragmentDetalleBinding? = null
    private val binding get() = _binding!!

    private val args: DetalleFragmentArgs by navArgs()
    private val viewModel: DetalleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        // Carga el libro — operación asíncrona, no bloquea el hilo principal
        viewModel.cargarLibro(args.libroId)

        viewModel.cargando.observe(viewLifecycleOwner) { cargando ->
            binding.progressBarDetalle.visibility = if (cargando) View.VISIBLE else View.GONE
        }

        viewModel.libro.observe(viewLifecycleOwner) { libro ->
            if (libro == null) return@observe

            binding.txtTituloDetalle.text    = libro.titulo
            binding.txtIsbn.text             = "ISBN: ${libro.isbn}"
            binding.txtAutoresDetalle.text   = libro.autoresString()
            binding.txtEditorial.text        = libro.editorial ?: "—"
            binding.txtEdicion.text          = libro.edicion ?: "—"
            binding.txtAnioDetalle.text      = libro.anioPub?.toString() ?: "—"
            binding.txtCategoriaDetalle.text = libro.categoria ?: "—"
            binding.txtIdioma.text           = libro.idioma ?: "—"
            binding.txtCopias.text           = libro.copias.size.toString()
            binding.txtDisponibles.text      = libro.disponibles().toString()

            if (libro.estaDisponible()) {
                binding.txtEstadoDetalle.text = "● Disponible"
                binding.txtEstadoDetalle.setTextColor(requireContext().getColor(R.color.green_disponible))
            } else {
                binding.txtEstadoDetalle.text = "● No disponible"
                binding.txtEstadoDetalle.setTextColor(requireContext().getColor(R.color.red_nodisponible))
            }

            if (!libro.subtitulo.isNullOrBlank()) {
                binding.txtSubtitulo.text       = libro.subtitulo
                binding.txtSubtitulo.visibility = View.VISIBLE
            }

            val firstCopia = libro.copias.firstOrNull()
            if (!firstCopia?.pasillo.isNullOrBlank()) {
                binding.layoutPasillo.visibility = View.VISIBLE
                binding.txtPasillo.text = firstCopia?.pasillo
            }
            if (!firstCopia?.estante.isNullOrBlank()) {
                binding.layoutEstante.visibility = View.VISIBLE
                binding.txtEstante.text = firstCopia?.estante
            }

            if (!libro.descripcion.isNullOrBlank()) {
                binding.layoutDescripcion.visibility = View.VISIBLE
                binding.txtDescripcion.text = libro.descripcion
            }

            Glide.with(this)
                .load(libro.portadaUrl)
                .placeholder(R.drawable.bg_portada_placeholder)
                .into(binding.imgPortadaDetalle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
