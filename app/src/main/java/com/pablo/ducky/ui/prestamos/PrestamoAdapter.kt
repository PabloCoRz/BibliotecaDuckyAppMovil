package com.pablo.ducky.ui.prestamos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pablo.ducky.R
import com.pablo.ducky.data.local.PrestamoEntity
import com.pablo.ducky.databinding.ItemPrestamoBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PrestamoAdapter : ListAdapter<PrestamoEntity, PrestamoAdapter.VH>(DIFF) {

    inner class VH(val binding: ItemPrestamoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        ItemPrestamoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        val p   = getItem(position)
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val b   = holder.binding
        val ctx = holder.itemView.context

        b.txtTituloPrestamo.text  = p.tituloLibro
        b.txtAutorPrestamo.text   = p.autorLibro
        b.txtFechaSolicitud.text  = "Solicitado: ${sdf.format(Date(p.fechaSolicitud))}"
        b.txtFechaVencimiento.text = "Vence: ${sdf.format(Date(p.fechaVencimiento))}"

        val vencido = System.currentTimeMillis() > p.fechaVencimiento
        if (vencido) {
            b.txtEstadoPrestamo.text = "● Vencido"
            b.txtEstadoPrestamo.setTextColor(ContextCompat.getColor(ctx, R.color.red_nodisponible))
        } else {
            b.txtEstadoPrestamo.text = "● Activo"
            b.txtEstadoPrestamo.setTextColor(ContextCompat.getColor(ctx, R.color.green_disponible))
        }

        Glide.with(b.imgPortadaPrestamo)
            .load(p.portadaUrl)
            .placeholder(R.drawable.bg_portada_placeholder)
            .into(b.imgPortadaPrestamo)
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<PrestamoEntity>() {
            override fun areItemsTheSame(a: PrestamoEntity, b: PrestamoEntity) = a.id == b.id
            override fun areContentsTheSame(a: PrestamoEntity, b: PrestamoEntity) = a == b
        }
    }
}
