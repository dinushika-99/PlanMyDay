package com.example.planmyday.ui.mood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planmyday.R
import com.example.planmyday.data.MoodRepository
import com.example.planmyday.model.MoodEntry
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MoodFragment : Fragment() {

    private lateinit var moodRepository: MoodRepository
    private lateinit var moodAdapter: MoodAdapter
    private lateinit var rvMoodList: RecyclerView
    private lateinit var fabAddMood: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mood, container, false)

        moodRepository = MoodRepository(requireContext())
        rvMoodList = view.findViewById(R.id.rvMoodList)
        fabAddMood = view.findViewById(R.id.fabAddMood)

        setupRecyclerView()
        loadMoodEntries()

        fabAddMood.setOnClickListener {
            showAddMoodDialog()
        }

        return view
    }

    private fun setupRecyclerView() {
        moodAdapter = MoodAdapter(emptyList())
        rvMoodList.layoutManager = LinearLayoutManager(requireContext())
        rvMoodList.adapter = moodAdapter
    }

    private fun loadMoodEntries() {
        val entries = moodRepository.getMoodEntries()
        moodAdapter.updateData(entries)
    }

    private fun showAddMoodDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_mood, null)
        val etMoodNote = dialogView.findViewById<EditText>(R.id.etMoodNote)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("How are you feeling?")
            .setView(dialogView)
            .setPositiveButton("Save Mood") { dialogInterface, _ ->
                // We'll handle this differently - see below
                dialogInterface.dismiss()
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()

        // Add custom mood buttons layout
        val moodButtonsView = LayoutInflater.from(requireContext()).inflate(R.layout.mood_buttons, null)
        dialog.setView(moodButtonsView)

        // Set up mood button clicks
        moodButtonsView.findViewById<View>(R.id.btnMoodSad).setOnClickListener {
            saveMoodEntry("😢", etMoodNote.text.toString())
            dialog.dismiss()
        }
        moodButtonsView.findViewById<View>(R.id.btnMoodNeutral).setOnClickListener {
            saveMoodEntry("😐", etMoodNote.text.toString())
            dialog.dismiss()
        }
        moodButtonsView.findViewById<View>(R.id.btnMoodHappy).setOnClickListener {
            saveMoodEntry("😊", etMoodNote.text.toString())
            dialog.dismiss()
        }
        moodButtonsView.findViewById<View>(R.id.btnMoodVeryHappy).setOnClickListener {
            saveMoodEntry("😄", etMoodNote.text.toString())
            dialog.dismiss()
        }
        moodButtonsView.findViewById<View>(R.id.btnMoodExcited).setOnClickListener {
            saveMoodEntry("🥳", etMoodNote.text.toString())
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun saveMoodEntry(emoji: String, note: String) {
        if (emoji.isNotEmpty()) {
            val moodEntry = MoodEntry(emoji = emoji, note = note)
            moodRepository.saveMoodEntry(moodEntry)
            loadMoodEntries()
            Toast.makeText(context, "Mood saved!", Toast.LENGTH_SHORT).show()
        }
    }
}