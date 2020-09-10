package com.ramzan.dicegainz.ui.lifts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ramzan.dicegainz.database.Lift
import com.ramzan.dicegainz.databinding.ListItemLiftBinding


class LiftAdapter( private val onClickListener: OnClickListener) :
    ListAdapter<Lift, LiftAdapter.ViewHolder>(LiftDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(item)
        }
        holder.bind(item)
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Lift]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Lift]
     */
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

