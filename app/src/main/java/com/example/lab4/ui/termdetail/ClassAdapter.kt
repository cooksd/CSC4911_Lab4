package com.example.lab4.ui.termdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lab4.R
import com.example.lab4.data.entities.Transcript

class ClassAdapter(
    private val onDelete: (Transcript) -> Unit
) : ListAdapter<Transcript, ClassAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view, onDelete)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        itemView: View,
        private val onDelete: (Transcript) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val titleView: TextView = itemView.findViewById(android.R.id.text1)
        private val gradeView: TextView = itemView.findViewById(android.R.id.text2)

        fun bind(transcript: Transcript) {
            titleView.text = transcript.courseDetailId
            gradeView.text = "Grade: ${transcript.grade}"
            itemView.setOnClickListener {
                onDelete(transcript)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Transcript>() {
        override fun areItemsTheSame(old: Transcript, new: Transcript) =
            old.transcriptDetailId == new.transcriptDetailId

        override fun areContentsTheSame(old: Transcript, new: Transcript) =
            old == new
    }
}