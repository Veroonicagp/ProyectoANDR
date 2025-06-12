package com.example.readytoenjoy.ui.adven.activitiesAdvenList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.databinding.ActivityListItemBinding

class ActivitiesAdvenListAdapter(
    private val onActivityClick: (Activity) -> Unit
) : ListAdapter<Activity, ActivitiesAdvenListAdapter.ActivitiesAdvenListViewHolder>(ActivitiesAdvenDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivitiesAdvenListViewHolder {
        val binding = ActivityListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ActivitiesAdvenListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivitiesAdvenListViewHolder, position: Int) {
        val activity = getItem(position)
        holder.bind(activity)
    }

    inner class ActivitiesAdvenListViewHolder(
        private val binding: ActivityListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(activity: Activity) {
            setupActivityInfo(activity)
            setupClickListener(activity)
            loadActivityImage(activity)
        }

        private fun setupActivityInfo(activity: Activity) {
            binding.crdTitle.text = activity.title
        }

        private fun setupClickListener(activity: Activity) {
            binding.root.setOnClickListener {
                onActivityClick(activity)
            }
        }

        private fun loadActivityImage(activity: Activity) {
            activity.img?.let { imageUri ->
                binding.crdImg.load(imageUri)
            }
        }
    }

    object ActivitiesAdvenDiffCallback : DiffUtil.ItemCallback<Activity>() {
        override fun areItemsTheSame(oldItem: Activity, newItem: Activity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Activity, newItem: Activity): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.location == newItem.location &&
                    oldItem.price == newItem.price
        }
    }
}