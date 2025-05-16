package com.example.lab4.ui.overview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lab4.R
import com.example.lab4.data.entities.Term
import java.text.SimpleDateFormat
import java.util.*

class TermAdapter(
    private val onClick: (Term) -> Unit
) : ListAdapter<Term, TermAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        itemView: View,
        private val onClick: (Term) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(android.R.id.text1)
        private val subtitle: TextView = itemView.findViewById(android.R.id.text2)
        private var currentTerm: Term? = null

        fun bind(term: Term) {
            currentTerm = term
            title.text = term.name
            subtitle.text = SimpleDateFormat("MM dd, yyyy", Locale.getDefault())
                .format(Date(term.startDate))
            itemView.setOnClickListener { onClick(term) }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Term>() {
        override fun areItemsTheSame(old: Term, new: Term) = old.termId == new.termId
        override fun areContentsTheSame(old: Term, new: Term) = old == new
    }
}