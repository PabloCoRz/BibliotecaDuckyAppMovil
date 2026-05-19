package com.pablo.ducky.ui.multas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pablo.ducky.data.local.MultaEntity
import com.pablo.ducky.databinding.ItemMultaBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MultaAdapter : ListAdapter<MultaEntity, MultaAdapter.VH>(DIFF) {

    inner class VH(val binding: ItemMultaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        ItemMultaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        val m   = getItem(position)
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val b   = holder.binding

        b.txtTituloMulta.text   = m.tituloLibro
        b.txtFechaMulta.text    = "Generada: ${sdf.format(Date(m.fechaGenerada))}"
        b.txtDiasRetraso.text   = "${m.diasRetraso} día(s) de retraso"
        b.txtMontoMulta.text    = "$${String.format("%.2f", m.monto)} MXN"
        b.txtEstadoMulta.text   = if (m.pagada) "✓ Pagada" else "⚠ Pendiente"
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<MultaEntity>() {
            override fun areItemsTheSame(a: MultaEntity, b: MultaEntity) = a.id == b.id
            override fun areContentsTheSame(a: MultaEntity, b: MultaEntity) = a == b
        }
    }
}
