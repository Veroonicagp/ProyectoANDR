package com.example.readytoenjoy.ui.myActivities

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.databinding.MyActivityListItemBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MyActivityListAdapter(private val toActivityDetail:((Activity)->Unit), private val onDeleteActivity: ((Activity) -> Unit)): ListAdapter<Activity, MyActivityListAdapter.MyActivityViewHolder>(
    MyActivityDiffCallback
) {

    inner class MyActivityViewHolder(private val binding: MyActivityListItemBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(activity: Activity){
            binding.crdTitle.text=activity.title
            if (activity.img!=null) {
                binding.crdImg.load(activity.img)
            }
            binding.deleteButton.setOnClickListener{
                MaterialAlertDialogBuilder(binding.root.context)
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Estás seguro de que deseas eliminar la actividad '${activity.title}'?")
                    .setPositiveButton("Eliminar") { _, _ ->
                        onDeleteActivity(activity)
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
            binding.root.setOnClickListener  {
                toActivityDetail(activity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyActivityViewHolder {
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