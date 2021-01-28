package com.nazmar.dicegainz.ui.lifts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nazmar.dicegainz.database.Lift
import com.nazmar.dicegainz.databinding.ListItemLiftBinding


class LiftAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Lift, LiftAdapter.ViewHolder>(LiftDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(item)
        }
        holder.bind(item)
    }

    class OnClickListener(val clickListener: (lift: Lift) -> Unit) {
        fun onClick(lift: Lift) = clickListener(lift)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ListItemLiftBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Lift) {
            binding.liftName.text = item.name
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)

                val binding =
                    ListItemLiftBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class LiftDiffCallback : DiffUtil.ItemCallback<Lift>() {
    override fun areItemsTheSame(oldItem: Lift, newItem: Lift): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Lift, newItem: Lift): Boolean {
        return oldItem == newItem
    }
}

