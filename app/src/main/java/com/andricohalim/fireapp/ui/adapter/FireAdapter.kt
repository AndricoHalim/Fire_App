package com.andricohalim.fireapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andricohalim.fireapp.R
import com.andricohalim.fireapp.data.response.FireResponse
import com.andricohalim.fireapp.data.response.SensorDataItem
import com.andricohalim.fireapp.databinding.ListItemBinding

class FireAdapter(private val listFire: List<SensorDataItem>) :
    RecyclerView.Adapter<FireAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class ViewHolder(private var binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SensorDataItem) {
            binding.apply {
                // 🔥 Mengubah background jika api terdeteksi
                if (data.flameStatus == "Api Terdeteksi") {
                    constraintLayout.setBackgroundResource(R.drawable.background_red)
                } else {
                    constraintLayout.setBackgroundResource(R.drawable.background)
                }

                tvTemperature.text = "${data.temperature}°C"
                tvFireStatus.text = when (data.flameStatus) {
                    "Api Terdeteksi" -> "Api\nTerdeteksi"
                    else -> "Aman\nTerkendali"
                }
                tvID.text = data.id.toString()
            }
        }
    }

    override fun getItemCount(): Int = listFire.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFire[position])
    }
}
