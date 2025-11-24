package com.example.planmyday.ui.habit

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planmyday.R
import com.example.planmyday.model.Habit

class HabitAdapter(
    private var habits: List<Habit>,
    private val onEditClick: (Habit) -> Unit,
    private val onDeleteClick: (Habit) -> Unit,
    private val onToggleComplete: (Habit) -> Unit
) : RecyclerView.Adapter<HabitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_habit, parent, false)
        return HabitViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habits[position]

        // Remove previous listeners to prevent duplicates:cite[9]
        holder.btnLike.setOnCheckedChangeListener(null)

        holder.textHabit.text = habit.name
        holder.btnLike.isChecked = habit.isCompleted

        // Set checked change listener instead of click listener
        holder.btnLike.setOnCheckedChangeListener { _, isChecked ->
            val currentHabit = habits[position]
            if (currentHabit.isCompleted != isChecked) {
                onToggleComplete(currentHabit)
            }
        }

        holder.btnEdit.setOnClickListener {
            onEditClick(habits[position])
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClick(habits[position])
        }

        holder.itemView.setOnLongClickListener {
            onDeleteClick(habits[position])
            true
        }
    }

    override fun getItemCount(): Int = habits.size

    fun updateData(newHabits: List<Habit>) {
        habits = newHabits
        notifyDataSetChanged()
    }
}