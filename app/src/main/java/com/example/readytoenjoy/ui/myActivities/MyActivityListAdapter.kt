package com.example.readytoenjoy.ui.myActivities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.databinding.MyActivityListItemBinding

//
class MyActivityListAdapter(private val toActivityDetail:((Activity)->Unit)): ListAdapter<Activity, MyActivityListAdapter.MyActivityViewHolder>(
    MyActivityDiffCallback
) {

    inner class MyActivityViewHolder(private val binding: MyActivityListItemBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(activity: Activity){
            binding.crdTitle.text=activity.title
            binding.crdLocation.text=activity.location
            binding.crdPrice.text=activity.price
            binding.deleteButton.setOnClickListener{
                //TODO hacer una confirmacion de borrado
            }
            binding.root.setOnClickListener  {
                toActivityDetail(activity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyActivityViewHolder {
        //intanciamos el xml y lo pasamos al itemViewHolder
        val binding: MyActivityListItemBinding = MyActivityListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyActivityViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyActivityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    object MyActivityDiffCallback: DiffUtil.ItemCallback<Activity>(){
        override fun areItemsTheSame(oldItem: Activity, newItem: Activity) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Activity, newItem: Activity) =
            oldItem.title == newItem.title &&
                    oldItem.location == newItem.location &&
                    oldItem.price == newItem.price

    }
}