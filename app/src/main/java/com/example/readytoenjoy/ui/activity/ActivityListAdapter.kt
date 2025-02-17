package com.example.readytoenjoy.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.databinding.ActivityListItemBinding
//
class ActivityListAdapter(private val toActivityDetail:((Activity)->Unit)): ListAdapter<Activity, ActivityListAdapter.ActivityViewHolder>(
    ActivityDiffCallback
) {

    inner class ActivityViewHolder(private val binding: ActivityListItemBinding):
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
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        //intanciamos el xml y lo pasamos al itemViewHolder
        val binding: ActivityListItemBinding = ActivityListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    object ActivityDiffCallback: DiffUtil.ItemCallback<Activity>(){
        override fun areItemsTheSame(oldItem: Activity, newItem: Activity) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Activity, newItem: Activity) =
                    oldItem.title == newItem.title &&
                    oldItem.location == newItem.location &&
                    oldItem.price == newItem.price

    }
}