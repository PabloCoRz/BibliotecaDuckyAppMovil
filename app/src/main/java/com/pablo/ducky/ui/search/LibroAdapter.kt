package com.pablo.ducky.ui.search

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pablo.ducky.R
import com.pablo.ducky.data.model.Libro
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LibroAdapter(
    private val onClick: (Libro) -> Unit
) : ListAdapter<Libro, LibroAdapter.ViewHolder>(DIFF) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val portada:   ImageView = view.findViewById(R.id.imgPortada)
        val titulo:    TextView  = view.findViewById(R.id.txtTitulo)
        val autores:   TextView  = view.findViewById(R.id.txtAutores)
        val categoria: TextView  = view.findViewById(R.id.txtCategoria)
        val año:       TextView  = view.findViewById(R.id.txtAnio)
        val estado:    TextView  = view.findViewById(R.id.txtEstado)
        val btnPrestamo: TextView = view.findViewById(R.id.btnSolicitarPrestamo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_libro, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val libro = getItem(position)
        holder.titulo.text    = libro.titulo
        holder.autores.text   = libro.autoresString()
        holder.categoria.text = libro.categoria ?: ""
        holder.año.text       = libro.anioPub?.toString() ?: "—"

        if (libro.estaDisponible()) {
            holder.estado.text = "● Disponible"
            holder.estado.setTextColor(
                holder.itemView.context.getColor(R.color.green_disponible)
            )
            holder.btnPrestamo.visibility = View.VISIBLE
        } else {
            holder.estado.text = "● No disponible"
            holder.estado.setTextColor(
                holder.itemView.context.getColor(R.color.red_nodisponible)
            )
            holder.btnPrestamo.visibility = View.GONE
        }

        Glide.with(holder.portada)
            .load(libro.portadaUrl)
            .placeholder(R.drawable.bg_portada_placeholder)
            .into(holder.portada)

        holder.itemView.setOnClickListener { onClick(libro) }

        holder.btnPrestamo.setOnClickListener {
            showPrestamoDialog(holder.itemView, libro)
        }
    }

    private fun showPrestamoDialog(anchor: View, libro: Libro) {
        val ctx = anchor.context
        val dialog = Dialog(ctx)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogView = LayoutInflater.from(ctx)
            .inflate(R.layout.dialog_prestamo, null)
        dialog.setContentView(dialogView)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // dates
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val today = Calendar.getInstance()
        val devolucion = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 8) }

        dialogView.findViewById<TextView>(R.id.txtFechaSolicitud).text  = sdf.format(today.time)
        dialogView.findViewById<TextView>(R.id.txtFechaDevolucion).text = sdf.format(devolucion.time)
        dialogView.findViewById<TextView>(R.id.txtTituloDialog).text    = libro.titulo
        dialogView.findViewById<TextView>(R.id.txtSubtituloDialog).text = libro.subtitulo ?: ""
        dialogView.findViewById<TextView>(R.id.txtAutorDialog).text     = libro.autoresString()
        dialogView.findViewById<TextView>(R.id.txtDisponiblesDialog).text =
            "● Disponible · ${libro.disponibles()} ejemplares"

        // cover
        Glide.with(ctx)
            .load(libro.portadaUrl)
            .placeholder(R.drawable.bg_portada_placeholder)
            .into(dialogView.findViewById(R.id.imgPortadaDialog))

        // spinner
        val opciones = listOf("WhatsApp", "Correo Electrónico", "SMS")
        val spinnerAdapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_item, opciones)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogView.findViewById<Spinner>(R.id.spinnerComprobante).adapter = spinnerAdapter

        // buttons
        dialogView.findViewById<TextView>(R.id.btnHacerReserva).setOnClickListener {
            dialog.dismiss()
            Toast.makeText(
                ctx,
                "¡Préstamo solicitado! Recibirás tu comprobante pronto.",
                Toast.LENGTH_LONG
            ).show()
        }

        dialogView.findViewById<TextView>(R.id.btnCancelar).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Libro>() {
            override fun areItemsTheSame(a: Libro, b: Libro) = a.id == b.id
            override fun areContentsTheSame(a: Libro, b: Libro) = a == b
        }
    }
}