package com.example.readytoenjoy.ui.adven

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.readytoenjoy.core.model.Adven
import com.example.readytoenjoy.databinding.AdvenListItemBinding

class AdvenListAdapter(): ListAdapter<Adven, AdvenListAdapter.AdvenViewHolder>(AdvenDiffCallBack) {

    inner class AdvenViewHolder(private val binding: AdvenListItemBinding):
            RecyclerView.ViewHolder(binding.root){
                fun bind(adven: Adven){
                    binding.nombre.text=adven.name
                    binding.profileImage.load(adven.media)
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvenViewHolder {
        val binding: AdvenListItemBinding = AdvenListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AdvenViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdvenViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object AdvenDiffCallBack: DiffUtil.ItemCallback<Adven>(){
        override fun areItemsTheSame(oldItem: Adven, newItem: Adven) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Adven, newItem: Adven) =
                    oldItem.name == newItem.name


    }
}