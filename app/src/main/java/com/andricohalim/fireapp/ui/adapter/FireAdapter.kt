package com.andricohalim.fireapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.andricohalim.fireapp.R
import com.andricohalim.fireapp.data.model.DataFire
import com.andricohalim.fireapp.databinding.ListItemBinding

class FireAdapter(private val dataHistory: ArrayList<DataFire>, private val onLocationClicked: (String) -> Unit) : RecyclerView.Adapter<FireAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataFire, deviceId: String) {
            binding.apply {
                if (data.flameDetected == "Api Terdeteksi") {
                    constraintLayout.background = null

                    // Ganti warna latar belakang MaterialCardView menjadi merah
                    cardView.setCardBackgroundColor(Color.RED)
                } else {
                    constraintLayout.setBackgroundResource(R.drawable.background)

                    cardView.setCardBackgroundColor(Color.WHITE)
                }

                // Tetapkan data lainnya
                tvTemperature.text = "${data.temp}°C"
                tvFireStatus.text = when (data.flameDetected) {
                    "Api Terdeteksi" -> "Api\nTerdeteksi"
                    else -> "Aman\nTerkendali"
                }
                tvID.text = "Device ID: $deviceId"

                btnLocation.setOnClickListener {
                    onLocationClicked(deviceId)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = dataHistory.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = dataHistory[position]
        holder.bind(data, data.deviceId)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<DataFire>) {
        for (newItem in newData) {
            val index = dataHistory.indexOfFirst { it.deviceId == newItem.deviceId }
            if (index != -1) {
                dataHistory[index] = newItem
            } else {
                dataHistory.add(newItem)
            }
        }

        // Periksa apakah ada api terdeteksi
        val hasFireDetected = dataHistory.any { it.flameDetected == "Api Terdeteksi" }

        // Jika ada api terdeteksi, sort dengan item api terdeteksi di atas
        if (hasFireDetected) {
            dataHistory.sortByDescending { it.flameDetected == "Api Terdeteksi" }
        } else {
            // Jika tidak ada api terdeteksi, sort berdasarkan deviceId
            dataHistory.sortBy { it.deviceId }
        }

        notifyDataSetChanged()
    }
}
