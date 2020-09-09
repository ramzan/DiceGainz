package com.ramzan.dicegainz.ui.lifts

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ramzan.dicegainz.R
import com.ramzan.dicegainz.TextItemViewHolder
import com.ramzan.dicegainz.dummy.DummyContent


class LiftRecyclerAdapter(val data: MutableList<DummyContent.DummyItem>): RecyclerView.Adapter<TextItemViewHolder>() {
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = layoutInflater
            .inflate(R.layout.lift_list_item_view, parent, false) as TextView

        return TextItemViewHolder(view)
    }
}