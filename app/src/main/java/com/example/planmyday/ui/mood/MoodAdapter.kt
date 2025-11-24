package com.example.planmyday.ui.mood

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planmyday.R
import com.example.planmyday.model.MoodEntry
import java.text.SimpleDateFormat
import java.util.Locale

class MoodAdapter(private var moodEntries: List<MoodEntry>) :
    RecyclerView.Adapter<MoodAdapter.MoodViewHolder>() {

    class MoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEmoji: TextView = itemView.findViewById(R.id.tvEmoji)

        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mood, parent, false)
        return MoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        val entry = moodEntries[position]
        holder.tvEmoji.text = entry.emoji

        val dateFormat = SimpleDateFormat("MMM dd, yyyy - HH:mm", Locale.getDefault())
        holder.tvDate.text = dateFormat.format(entry.date)
    }

    override fun getItemCount(): Int = moodEntries.size

    fun updateData(newEntries: List<MoodEntry>) {
        moodEntries = newEntries
        notifyDataSetChanged()
    }
}