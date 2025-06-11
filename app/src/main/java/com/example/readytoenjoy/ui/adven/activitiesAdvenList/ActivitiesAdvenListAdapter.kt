package com.example.readytoenjoy.ui.adven.activitiesAdvenList

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.databinding.ActivityListItemBinding
import com.example.readytoenjoy.ui.activity.ActivityListAdapter.ActivityViewHolder

class ActivitiesAdvenListAdapter(private val toActivityDetail:((Activity)->Unit)): ListAdapter<Activity, ActivitiesAdvenListAdapter.ActivitiesAdvenListViewHolder>(ActivitiesAdvenDiffCallback){

    inner class ActivitiesAdvenListViewHolder(private val binding: ActivityListItemBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(activity: Activity){
            binding.crdTitle.text=activity.title
            binding.crdLocation.text=activity.location
            binding.crdPrice.text=activity.price
            binding.root.setOnClickListener  {
                toActivityDetail(activity)
            }
            if (activity.img!=null) {
                binding.crdImg.load(activity.img)
            } else {
                Log.w("ImageLoading", "No image URL for this activity")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivitiesAdvenListViewHolder {
        val binding: ActivityListItemBinding = ActivityListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ActivitiesAdvenListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivitiesAdvenListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object ActivitiesAdvenDiffCallback: DiffUtil.ItemCallback<Activity>(){
        override fun areItemsTheSame(oldItem: Activity, newItem: Activity) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Activity, newItem: Activity) =
            oldItem.title == newItem.title &&
                    oldItem.location == newItem.location &&
                    oldItem.price == newItem.price

    }

}