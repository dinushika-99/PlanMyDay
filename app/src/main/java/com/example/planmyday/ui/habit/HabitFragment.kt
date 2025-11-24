package com.example.planmyday.ui.habit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planmyday.R
import com.example.planmyday.data.HabitRepository
import com.example.planmyday.model.Habit
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import java.util.Calendar
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Date

class HabitFragment : Fragment() {

    private lateinit var habitRepository: HabitRepository
    private lateinit var habitAdapter: HabitAdapter
    private lateinit var rvList: RecyclerView
    private lateinit var btnAddItem: FloatingActionButton
    private lateinit var tvProgress: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvGreeting: TextView
    private lateinit var tvDate: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_habit, container, false)

        habitRepository = HabitRepository(requireContext())
        rvList = view.findViewById(R.id.rvList)
        btnAddItem = view.findViewById(R.id.btnAddItem)
        tvProgress = view.findViewById(R.id.tvProgress)
        progressBar = view.findViewById(R.id.progressBar)

        tvGreeting = view.findViewById(R.id.tvGreeting)
        tvDate = view.findViewById(R.id.tvDate)

        setupRecyclerView()
        loadHabits()
        setupDateAndGreeting()

        btnAddItem.setOnClickListener {
            showAddHabitDialog()
        }

        return view
    }

    private fun setupDateAndGreeting() {
        // Set current date
        val currentDate = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
        tvDate.text = currentDate

        // Set greeting based on time of day
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val greeting = when (hour) {
            in 5..11 -> "Good Morning! 🌅"
            in 12..17 -> "Good Afternoon! ☀️"
            in 18..21 -> "Good Evening! 🌙"
            else -> "Good Night! 🌙"
        }
        tvGreeting.text = greeting
    }

    private fun setupRecyclerView() {
        habitAdapter = HabitAdapter(
            habits = emptyList(),
            onEditClick = { habit -> showEditHabitDialog(habit) },
            onDeleteClick = { habit -> showDeleteConfirmationDialog(habit) },
            onToggleComplete = { habit -> toggleHabitCompletion(habit) }
        )

        rvList.layoutManager = LinearLayoutManager(requireContext())
        rvList.adapter = habitAdapter
    }

    private fun loadHabits() {

        var habits = habitRepository.getHabits()

        // Add default habits only if no habits exist
        if (habits.isEmpty()) {
            habits = listOf(
                Habit(name = "🌅 Wake up at 7 AM"),  // IDs will be auto-generated
                Habit(name = "🛏️ Make your bed EVER MORNING"),
                Habit(name = "🥛 Drink a BIG GLASS OF WATER as soon as you wake up"),
                Habit(name = "🏋️ Work out until you SWEET"),
            ).toMutableList()
            habitRepository.saveHabits(habits)
        }

        habitAdapter.updateData(habits)
        updateProgress(habits)
    }

    private fun toggleHabitCompletion(habit: Habit) {
        val habits = habitRepository.getHabits().toMutableList()
        val index = habits.indexOfFirst { it.id == habit.id }
        if (index != -1) {
            habits[index] = habit.copy(isCompleted = !habit.isCompleted)
            habitRepository.saveHabits(habits)
            habitAdapter.updateData(habits)
            updateProgress(habits)
        }
    }

    private fun showAddHabitDialog() {
        val input = EditText(requireContext())
        input.hint = "Enter new habit"

        AlertDialog.Builder(requireContext())
            .setTitle("Add New Habit")
            .setView(input)
            .setPositiveButton("Add") { _, _ ->
                val name = input.text.toString().trim()
                if (name.isNotEmpty()) {
                    val habits = habitRepository.getHabits().toMutableList()
                    habits.add(Habit(name = name))
                    habitRepository.saveHabits(habits)
                    habitAdapter.updateData(habits)
                    updateProgress(habits)
                } else {
                    Toast.makeText(context, "Habit name cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEditHabitDialog(habit: Habit) {
        val input = EditText(requireContext())
        input.setText(habit.name)
        input.hint = "Enter habit name"

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Habit")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotEmpty()) {
                    val habits = habitRepository.getHabits().toMutableList()
                    val index = habits.indexOfFirst { it.id == habit.id }
                    if (index != -1) {
                        habits[index] = habit.copy(name = newName)
                        habitRepository.saveHabits(habits)
                        habitAdapter.updateData(habits)
                    }
                } else {
                    Toast.makeText(context, "Habit name cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirmationDialog(habit: Habit) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Habit")
            .setMessage("Are you sure you want to delete '${habit.name}'?")
            .setPositiveButton("Delete") { _, _ ->
                val habits = habitRepository.getHabits().toMutableList()
                habits.removeAll { it.id == habit.id }     // Correctly identify the habit to delete by its unique ID
                habitRepository.saveHabits(habits)
                habitAdapter.updateData(habits)
                updateProgress(habits)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }



    private fun updateProgress(habits: List<Habit>) {
        val completed = habits.count { it.isCompleted }
        val total = habits.size
        val percentage = if (total > 0) (completed * 100) / total else 0

        // Show both percentage and count
        tvProgress.text = "$percentage% Daily Achievement "

        // Update progress bar
        progressBar?.progress = percentage
    }
}