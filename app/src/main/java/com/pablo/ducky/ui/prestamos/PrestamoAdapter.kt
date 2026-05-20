package com.pablo.ducky.ui.prestamos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pablo.ducky.R
import com.pablo.ducky.data.local.entity.PrestamoEntity
import com.pablo.ducky.databinding.ItemPrestamoBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PrestamoAdapter : ListAdapter<PrestamoEntity, PrestamoAdapter.ViewHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<PrestamoEntity>() {
            override fun areItemsTheSame(a: PrestamoEntity, b: PrestamoEntity) = a.id == b.id
            override fun areContentsTheSame(a: PrestamoEntity, b: PrestamoEntity) = a == b
        }
    }

    class ViewHolder(val binding: ItemPrestamoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPrestamoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val b = holder.binding
        val fmt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val ahora = System.currentTimeMillis()
        val ctx = holder.itemView.context

        b.txtTituloPrestamo.text = item.tituloLibro
        b.txtAutorPrestamo.text = item.autorLibro
        b.txtFechasPrestamo.text = "Solicitud: ${fmt.format(item.fechaSolicitud)}  |  Devolucion: ${fmt.format(item.fechaDevolucion)}"

        when {
            item.devuelto -> {
                b.txtEstadoPrestamo.text = "● Devuelto"
                b.txtEstadoPrestamo.setTextColor(ctx.getColor(R.color.stone))
            }
            ahora > item.fechaDevolucion -> {
                b.txtEstadoPrestamo.text = "● Vencido"
                b.txtEstadoPrestamo.setTextColor(ctx.getColor(R.color.red_nodisponible))
            }
            else -> {
                b.txtEstadoPrestamo.text = "● Activo"
                b.txtEstadoPrestamo.setTextColor(ctx.getColor(R.color.green_disponible))
            }
        }

        Glide.with(holder.itemView)
            .load(item.portadaUrl)
            .placeholder(R.drawable.bg_portada_placeholder)
            .into(b.imgPortadaPrestamo)
    }
}
