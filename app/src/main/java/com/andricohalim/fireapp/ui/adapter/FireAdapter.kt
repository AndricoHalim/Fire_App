package com.andricohalim.fireapp.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andricohalim.fireapp.data.model.DataFire
import com.andricohalim.fireapp.databinding.ListItemBinding

class FireAdapter(private val dataHistory: ArrayList<DataFire>) : RecyclerView.Adapter<FireAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataFire, deviceId: String) {
            binding.apply {
                tvTemperature.text = "${data.temp}°C"
                tvFireStatus.text = when (data.flameDetected) {
                    "Api Terdeteksi" -> "Terdeteksi"
                    else -> "Aman\nTerkendali"
                }
                tvID.text = "Device ID: $deviceId"

                // Ubah latar belakang menjadi merah jika api terdeteksi
                val backgroundColor = if (data.flameDetected == "Api Terdeteksi") {
                    Color.RED
                } else {
                    Color.WHITE
                }
                root.setBackgroundColor(backgroundColor)
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
        // Perbarui data yang ada atau tambahkan jika belum ada
        for (newItem in newData) {
            val index = dataHistory.indexOfFirst { it.deviceId == newItem.deviceId }
            if (index != -1) {
                // Update existing item
                dataHistory[index] = newItem
            } else {
                // Add new item if not found
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
