package com.pablo.ducky.ui.search

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pablo.ducky.R
import com.pablo.ducky.data.model.Libro
import com.pablo.ducky.databinding.DialogPrestamoBinding
import com.pablo.ducky.databinding.ItemLibroBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LibroAdapter(
    private val onClick: (Libro) -> Unit,
    private val onReserva: (Libro) -> Unit = {}
) : ListAdapter<Libro, LibroAdapter.ViewHolder>(DIFF) {

    inner class ViewHolder(val binding: ItemLibroBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLibroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val libro = getItem(position)
        val b = holder.binding

        b.txtTitulo.text = libro.titulo
        b.txtAutores.text = libro.autoresString()
        b.txtCategoria.text = libro.categoria ?: ""
        b.txtAnio.text = libro.anioPub?.toString() ?: "—"

        if (libro.estaDisponible()) {
            b.txtEstado.text = "● Disponible"
            b.txtEstado.setTextColor(holder.itemView.context.getColor(R.color.green_disponible))
            b.btnSolicitarPrestamo.visibility = android.view.View.VISIBLE
        } else {
            b.txtEstado.text = "● No disponible"
            b.txtEstado.setTextColor(holder.itemView.context.getColor(R.color.red_nodisponible))
            b.btnSolicitarPrestamo.visibility = android.view.View.GONE
        }

        Glide.with(b.imgPortada)
            .load(libro.portadaUrl)
            .placeholder(R.drawable.bg_portada_placeholder)
            .into(b.imgPortada)

        holder.itemView.setOnClickListener { onClick(libro) }

        b.btnSolicitarPrestamo.setOnClickListener {
            showPrestamoDialog(holder, libro)
        }
    }

    private fun showPrestamoDialog(holder: ViewHolder, libro: Libro) {
        val ctx = holder.itemView.context
        val dialog = Dialog(ctx)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogPrestamoBinding.inflate(LayoutInflater.from(ctx))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val today = Calendar.getInstance()
        val devolucion = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 8) }

        dialogBinding.txtFechaSolicitud.text = sdf.format(today.time)
        dialogBinding.txtFechaDevolucion.text = sdf.format(devolucion.time)
        dialogBinding.txtTituloDialog.text = libro.titulo
        dialogBinding.txtSubtituloDialog.text = libro.subtitulo ?: ""
        dialogBinding.txtAutorDialog.text = libro.autoresString()
        dialogBinding.txtDisponiblesDialog.text = "● Disponible · ${libro.disponibles()} ejemplares"

        Glide.with(ctx)
            .load(libro.portadaUrl)
            .placeholder(R.drawable.bg_portada_placeholder)
            .into(dialogBinding.imgPortadaDialog)

        val opciones = listOf("WhatsApp", "Correo Electronico", "SMS")
        val spinnerAdapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_item, opciones)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogBinding.spinnerComprobante.adapter = spinnerAdapter

        dialogBinding.btnHacerReserva.setOnClickListener {
            onReserva(libro)
            dialog.dismiss()
            Toast.makeText(ctx, "Prestamo solicitado!", Toast.LENGTH_LONG).show()
        }
        dialogBinding.btnCancelar.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Libro>() {
            override fun areItemsTheSame(a: Libro, b: Libro) = a.id == b.id
            override fun areContentsTheSame(a: Libro, b: Libro) = a == b
        }
    }
}
