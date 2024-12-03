package com.andricohalim.fireapp.ui.adapter

import android.annotation.SuppressLint
import android.app.LauncherActivity.ListItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andricohalim.fireapp.data.model.DataFire
import com.andricohalim.fireapp.databinding.ListItemBinding

class ListAdapter(private val dataHistory: ArrayList<DataFire>): RecyclerView.Adapter<com.andricohalim.fireapp.ui.adapter.ListAdapter.ListViewHolder>(){
    inner class ListViewHolder(private val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataFire, deviceId: String) {
            binding.apply {
                tvTemperature.text = "${data.temp}°C"
                tvFireStatus.text = when (data.flameDetected) {
                    "Api Terdeteksi" -> "Terdeteksi"
                    else -> "Aman\nTerkendali"
                }
                tvID.text = "Device ID: $deviceId" // Tambahkan TextView di layout untuk ID perangkat
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataHistory.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = dataHistory[position]
        holder.bind(data, data.deviceId) // Pastikan `DataFire` memiliki properti `deviceId`.
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<DataFire>){
        dataHistory.clear()
        dataHistory.addAll(newData)
        notifyDataSetChanged()
    }

}