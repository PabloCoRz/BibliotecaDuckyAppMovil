package com.pablo.ducky.ui.multas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pablo.ducky.data.local.entity.PrestamoEntity
import com.pablo.ducky.databinding.ItemMultaBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class MultaAdapter : ListAdapter<PrestamoEntity, MultaAdapter.ViewHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<PrestamoEntity>() {
            override fun areItemsTheSame(a: PrestamoEntity, b: PrestamoEntity) = a.id == b.id
            override fun areContentsTheSame(a: PrestamoEntity, b: PrestamoEntity) = a == b
        }
    }

    class ViewHolder(val binding: ItemMultaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMultaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val b = holder.binding
        val fmt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val ahora = System.currentTimeMillis()
        val diasRetraso = TimeUnit.MILLISECONDS.toDays(ahora - item.fechaDevolucion).toInt()

        b.txtTituloMulta.text = item.tituloLibro
        b.txtFechaVencimiento.text = "Vencio el ${fmt.format(item.fechaDevolucion)}"
        b.txtDiasRetraso.text = "$diasRetraso dia(s) de retraso"
        b.txtMonto.text = "$${"%.2f".format(diasRetraso * 5.0)} MXN"
    }
}
