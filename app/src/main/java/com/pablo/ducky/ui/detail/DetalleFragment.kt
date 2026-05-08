package com.pablo.ducky.ui.detail

import com.pablo.ducky.data.model.Libro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.pablo.ducky.R
import com.pablo.ducky.data.repository.LibroRepository
import kotlinx.coroutines.runBlocking

class DetalleFragment : Fragment() {

    private val args: DetalleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_detalle, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo  = LibroRepository()
        val libro = runBlocking {
            repo.buscar("").getOrNull()?.find { it.id == args.libroId }
        } ?: return

        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            findNavController().navigateUp()
        }

        view.findViewById<TextView>(R.id.txtTituloDetalle).text    = libro.titulo
        view.findViewById<TextView>(R.id.txtIsbn).text             = "ISBN: ${libro.isbn}"
        view.findViewById<TextView>(R.id.txtAutoresDetalle).text   = libro.autoresString()
        view.findViewById<TextView>(R.id.txtEditorial).text        = libro.editorial ?: "—"
        view.findViewById<TextView>(R.id.txtEdicion).text          = libro.edicion ?: "—"
        view.findViewById<TextView>(R.id.txtAnioDetalle).text      = libro.anioPub?.toString() ?: "—"
        view.findViewById<TextView>(R.id.txtCategoriaDetalle).text = libro.categoria ?: "—"
        view.findViewById<TextView>(R.id.txtIdioma).text           = libro.idioma ?: "—"
        view.findViewById<TextView>(R.id.txtCopias).text           = libro.copias.size.toString()
        view.findViewById<TextView>(R.id.txtDisponibles).text      = libro.disponibles().toString()

        val estadoView = view.findViewById<TextView>(R.id.txtEstadoDetalle)
        if (libro.estaDisponible()) {
            estadoView.text = "● Disponible"
            estadoView.setTextColor(requireContext().getColor(R.color.green_disponible))
        } else {
            estadoView.text = "● No disponible"
            estadoView.setTextColor(requireContext().getColor(R.color.red_nodisponible))
        }

        val subtituloView = view.findViewById<TextView>(R.id.txtSubtitulo)
        if (!libro.subtitulo.isNullOrBlank()) {
            subtituloView.text       = libro.subtitulo
            subtituloView.visibility = View.VISIBLE
        }

        val firstCopia = libro.copias.firstOrNull()
        if (!firstCopia?.pasillo.isNullOrBlank()) {
            view.findViewById<LinearLayout>(R.id.layoutPasillo).visibility = View.VISIBLE
            view.findViewById<TextView>(R.id.txtPasillo).text = firstCopia?.pasillo
        }
        if (!firstCopia?.estante.isNullOrBlank()) {
            view.findViewById<LinearLayout>(R.id.layoutEstante).visibility = View.VISIBLE
            view.findViewById<TextView>(R.id.txtEstante).text = firstCopia?.estante
        }

        if (!libro.descripcion.isNullOrBlank()) {
            view.findViewById<LinearLayout>(R.id.layoutDescripcion).visibility = View.VISIBLE
            view.findViewById<TextView>(R.id.txtDescripcion).text = libro.descripcion
        }

        Glide.with(this)
            .load(libro.portadaUrl)
            .placeholder(R.drawable.bg_portada_placeholder)
            .into(view.findViewById(R.id.imgPortadaDetalle))
    }
}