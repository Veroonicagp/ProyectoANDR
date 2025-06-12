package com.example.readytoenjoy.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.databinding.ActivityListItemBinding

class ActivityListAdapter(
    private val onActivityClick: (Activity) -> Unit
) : ListAdapter<Activity, ActivityListAdapter.ActivityViewHolder>(ActivityDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ActivityListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = getItem(position)
        holder.bind(activity)
    }

    inner class ActivityViewHolder(
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

    object ActivityDiffCallback : DiffUtil.ItemCallback<Activity>() {
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