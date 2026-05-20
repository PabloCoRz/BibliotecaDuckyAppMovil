package com.pablo.ducky.ui.detail

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.pablo.ducky.R
import com.pablo.ducky.data.local.entity.PrestamoEntity
import com.pablo.ducky.data.model.Libro
import com.pablo.ducky.databinding.DialogPrestamoBinding
import com.pablo.ducky.databinding.FragmentDetalleBinding
import java.text.SimpleDateFormat
import java.util.Locale

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

        viewModel.libro.observe(viewLifecycleOwner) { libro ->
            libro ?: return@observe
            bind(libro)
        }

        viewModel.prestamoGuardado.observe(viewLifecycleOwner) { guardado ->
            if (guardado == true) {
                Toast.makeText(requireContext(), "Prestamo registrado exitosamente", Toast.LENGTH_SHORT).show()
                viewModel.resetPrestamoGuardado()
            }
        }

        viewModel.cargarLibro(args.libroId)
    }

    private fun bind(libro: Libro) {
        binding.txtTituloDetalle.text = libro.titulo
        binding.txtIsbn.text = "ISBN: ${libro.isbn}"
        binding.txtAutoresDetalle.text = libro.autoresString()
        binding.txtEditorial.text = libro.editorial ?: "—"
        binding.txtEdicion.text = libro.edicion ?: "—"
        binding.txtAnioDetalle.text = libro.anioPub?.toString() ?: "—"
        binding.txtCategoriaDetalle.text = libro.categoria ?: "—"
        binding.txtIdioma.text = libro.idioma ?: "—"
        binding.txtCopias.text = libro.copias.size.toString()
        binding.txtDisponibles.text = libro.disponibles().toString()

        if (libro.estaDisponible()) {
            binding.txtEstadoDetalle.text = "● Disponible"
            binding.txtEstadoDetalle.setTextColor(requireContext().getColor(R.color.green_disponible))
        } else {
            binding.txtEstadoDetalle.text = "● No disponible"
            binding.txtEstadoDetalle.setTextColor(requireContext().getColor(R.color.red_nodisponible))
        }

        if (!libro.subtitulo.isNullOrBlank()) {
            binding.txtSubtitulo.text = libro.subtitulo
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

        if (!libro.estaDisponible()) {
            binding.btnSolicitarPrestamo.alpha = 0.5f
            binding.btnSolicitarPrestamo.isEnabled = false
        }
        binding.btnSolicitarPrestamo.setOnClickListener {
            mostrarDialogPrestamo(libro)
        }
    }

    private fun mostrarDialogPrestamo(libro: Libro) {
        val dialogBinding = DialogPrestamoBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialogBinding.txtTituloDialog.text = libro.titulo
        dialogBinding.txtSubtituloDialog.text = libro.subtitulo ?: ""
        dialogBinding.txtAutorDialog.text = libro.autoresString()
        dialogBinding.txtDisponiblesDialog.text = "${libro.disponibles()} copia(s) disponible(s)"

        Glide.with(this)
            .load(libro.portadaUrl)
            .placeholder(R.drawable.bg_portada_placeholder)
            .into(dialogBinding.imgPortadaDialog)

        val ahora = System.currentTimeMillis()
        val devolucion = ahora + 8L * 24 * 60 * 60 * 1000
        val fmt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dialogBinding.txtFechaSolicitud.text = fmt.format(ahora)
        dialogBinding.txtFechaDevolucion.text = fmt.format(devolucion)

        val opciones = arrayOf("Email", "Mensaje de texto", "WhatsApp")
        dialogBinding.spinnerComprobante.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, opciones
        )

        dialogBinding.btnCancelar.setOnClickListener { dialog.dismiss() }

        dialogBinding.btnHacerReserva.setOnClickListener {
            viewModel.guardarPrestamo(
                PrestamoEntity(
                    libroId = libro.id,
                    tituloLibro = libro.titulo,
                    autorLibro = libro.autoresString(),
                    portadaUrl = libro.portadaUrl,
                    fechaSolicitud = ahora,
                    fechaDevolucion = devolucion
                )
            )
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
