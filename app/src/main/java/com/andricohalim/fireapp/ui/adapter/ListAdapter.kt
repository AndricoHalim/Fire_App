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
        fun bind(data: DataFire){
            binding.apply {
                tvTemperature.text = data.temp.toString()
                tvFireStatus.text = data.flameDetected.toString()
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
        holder.bind(dataHistory[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<DataFire>){
        dataHistory.clear()
        dataHistory.addAll(newData)
        notifyDataSetChanged()
    }

}