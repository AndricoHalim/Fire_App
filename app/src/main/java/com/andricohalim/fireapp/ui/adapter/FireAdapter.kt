package com.andricohalim.fireapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andricohalim.fireapp.R
import com.andricohalim.fireapp.data.model.DataFire
import com.andricohalim.fireapp.databinding.ListItemBinding

class FireAdapter(private val dataHistory: ArrayList<DataFire>, private val onLocationClicked: (String) -> Unit) : RecyclerView.Adapter<FireAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataFire, deviceId: String) {
            binding.apply {
                if (data.flameDetected == "Api Terdeteksi") {
                    constraintLayout.setBackgroundResource(R.drawable.background_red)
                } else {
                    constraintLayout.setBackgroundResource(R.drawable.background)
                }

                tvTemperature.text = "${data.temp}°C"
                tvFireStatus.text = when (data.flameDetected) {
                    "Api Terdeteksi" -> "Api\nTerdeteksi"
                    else -> "Aman\nTerkendali"
                }
                tvID.text = "$deviceId"

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

        val hasFireDetected = dataHistory.any { it.flameDetected == "Api Terdeteksi" }

        if (hasFireDetected) {
            dataHistory.sortByDescending { it.flameDetected == "Api Terdeteksi" }
        } else {
            dataHistory.sortBy { it.deviceId }
        }

        notifyDataSetChanged()
    }
}
