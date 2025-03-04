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
                false))
    }

    class ViewHolder(private var binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(data: SensorDataItem) {
            binding.tvID.text = data.id.toString()
            binding.tvFireStatus.text = data.flameStatus
            binding.tvTemperature.text = data.temperature.toString()

//            binding.root.setOnClickListener {
//                val detailIntent = Intent(binding.root.context, DetailActivity::class.java)
//                detailIntent.putExtra(DetailActivity.DETAIL_STORY, stories)
//                itemView.context.startActivity(detailIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity).toBundle())
//            }
        }
    }

    override fun getItemCount(): Int {
        return listFire.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(listFire[position])
    }
}