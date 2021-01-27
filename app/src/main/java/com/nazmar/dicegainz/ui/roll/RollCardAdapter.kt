package com.nazmar.dicegainz.ui.roll

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nazmar.dicegainz.R
import com.nazmar.dicegainz.databinding.ListItemRollCardBinding
import com.nazmar.dicegainz.ui.NoFilterAdapter


class RollCardAdapter(private val onClickListener: OnClickListener, val resources: Resources) :
    ListAdapter<RollCard, RollCardAdapter.ViewHolder>(RollCardDiffCallback()) {

    private var filterAdapter: NoFilterAdapter? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onRollClick(position)
        }
        holder.bind(
            item,
            resources.getString(R.string.roll_card_title, position + 1),
            resources.getString(R.string.tap_to_roll),
            resources.getString(R.string.all),
            filterAdapter,
            onClickListener::onFilterClick
        )
    }

    interface OnClickListener {
        fun onRollClick(position: Int)
        fun onFilterClick(position: Int, text: String)
    }

    fun setTagFilterAdapter(adapter: NoFilterAdapter) {
        filterAdapter = adapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ListItemRollCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: RollCard,
            title: String,
            tapToRollString: String,
            allTagString: String,
            filterAdapter: NoFilterAdapter?,
            onFilterClick: (Int, String) -> Unit,
        ) {
            binding.apply {
                cardNumber.text = title
                filter.setText(if (item.filterText.isEmpty()) allTagString else item.filterText)
                rollText.text = if (item.rollResult.isEmpty()) {
                    tapToRollString
                } else item.rollResult
                filterAdapter?.let {
                    filter.setAdapter(it)
                    filter.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
                        onFilterClick(item.id, filter.text.toString())
                        filter.clearFocus()
                    }
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)

                val binding =
                    ListItemRollCardBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class RollCardDiffCallback : DiffUtil.ItemCallback<RollCard>() {
    override fun areItemsTheSame(oldItem: RollCard, newItem: RollCard): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RollCard, newItem: RollCard): Boolean {
        return oldItem == newItem
    }
}

data class RollCard(
    val id: Int,
    var filterText: String = "",
    var rollResult: String = ""
)
