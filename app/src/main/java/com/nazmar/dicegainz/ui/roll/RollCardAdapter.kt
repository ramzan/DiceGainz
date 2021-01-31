package com.nazmar.dicegainz.ui.roll

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nazmar.dicegainz.R
import com.nazmar.dicegainz.databinding.ListItemRollCardBinding
import com.nazmar.dicegainz.ui.NoFilterAdapter

private const val ITEM_VIEW_TYPE_ROLL = 0
private const val ITEM_VIEW_TYPE_ADD = 1

class RollCardAdapter(private val onClickListener: OnClickListener, val resources: Resources) :
    ListAdapter<Card, RecyclerView.ViewHolder>(RollCardDiffCallback()) {

    private var filterAdapter: NoFilterAdapter? = null

    fun setTagFilterAdapter(adapter: NoFilterAdapter) {
        filterAdapter = adapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_ROLL -> ViewHolder.from(parent)
            ITEM_VIEW_TYPE_ADD -> AddCardViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    fun addAddCardAndSubmitList(list: List<Card>) {
        submitList(list + listOf(Card.AddCard))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is ViewHolder -> {
                holder.itemView.setOnClickListener {
                    onClickListener.onRoll(item.id)
                }
                holder.bind(
                    item,
                    resources.getString(R.string.tap_to_roll),
                    resources.getString(R.string.all),
                    filterAdapter,
                    onClickListener::onFilterChange,
                    onClickListener::onDeleteCard
                )
            }
            is AddCardViewHolder -> {
                holder.itemView.setOnClickListener {
                    onClickListener.onAddCard()
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Card.RollCard -> ITEM_VIEW_TYPE_ROLL
            is Card.AddCard -> ITEM_VIEW_TYPE_ADD
        }
    }

    interface OnClickListener {
        fun onRoll(id: Int)
        fun onFilterChange(id: Int, text: String)
        fun onAddCard()
        fun onDeleteCard(id: Int)
    }

    class AddCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): AddCardViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.addcard, parent, false)
                return AddCardViewHolder(view)
            }
        }
    }

    class ViewHolder private constructor(private val binding: ListItemRollCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Card,
            tapToRollString: String,
            allTagString: String,
            filterAdapter: NoFilterAdapter?,
            onFilterChange: (Int, String) -> Unit,
            onDeleteCard: (Int) -> Unit,
        ) {
            if (item is Card.RollCard) {
                binding.apply {
                    cardDeleteBtn.setOnClickListener {
                        onDeleteCard(item.id)
                    }

                    rollText.text =
                        if (item.rollResult.isNotEmpty()) item.rollResult else tapToRollString

                    filter.setText(if (item.filterText.isEmpty()) allTagString else item.filterText)
                    filterAdapter?.let {
                        filter.setAdapter(it)
                        filter.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
                            onFilterChange(item.id, filter.text.toString())
                            filter.clearFocus()
                        }
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


class RollCardDiffCallback : DiffUtil.ItemCallback<Card>() {
    override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem == newItem
    }
}


sealed class Card {
    abstract val id: Int

    data class RollCard(
        override val id: Int,
        val filterText: String = "",
        val rollResult: String = ""
    ) : Card()

    object AddCard : Card() {
        override val id = -1
    }
}

