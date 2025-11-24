package com.example.planmyday.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.planmyday.R
import com.example.planmyday.databinding.FragmentHomeBinding
import com.example.planmyday.data.MoodRepository
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var moodRepository: MoodRepository
    private lateinit var tvDate: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvDate = view.findViewById(R.id.tvDate)

        moodRepository = MoodRepository(requireContext())
        setupMoodChart()
        setupDateAndGreeting()
    }

    private fun setupDateAndGreeting() {
        // Set current date
        val currentDate = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
        tvDate.text = currentDate
    }

    private fun setupMoodChart() {
        val chart = binding.moodChart

        // Get last 7 days of mood data
        val moodEntries = getLastWeekMoodData()

        // Create line dataset
        val dataSet = LineDataSet(moodEntries, "Mood Level")
        dataSet.color = Color.parseColor("#050E42") // Your primary blue
        dataSet.lineWidth = 3f
        dataSet.setCircleColor(Color.parseColor("#050E42"))
        dataSet.circleRadius = 5f
        dataSet.setDrawCircleHole(true)
        dataSet.circleHoleRadius = 3f
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER // Smooth curves

        // Create line data
        val lineData = LineData(dataSet)
        chart.data = lineData

        // Configure chart appearance
        chart.description.isEnabled = false
        chart.legend.isEnabled = false

        // Configure X axis
        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val days = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                return days.getOrNull(value.toInt() - 1) ?: ""
            }
        }

        // Configure Y axis
        val leftAxis = chart.axisLeft
        leftAxis.setDrawGridLines(false)
        leftAxis.axisMinimum = 0f
        leftAxis.axisMaximum = 5f
        leftAxis.granularity = 1f
        leftAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val moods = arrayOf("😢", "😐", "😊", "😄", "🥳")
                return moods.getOrNull(value.toInt() - 1) ?: ""
            }
        }

        // Hide right Y axis
        chart.axisRight.isEnabled = false

        // Refresh chart
        chart.invalidate()
    }

    private fun getLastWeekMoodData(): List<Entry> {
        // Get real mood data from your repository
        val realMoods = moodRepository.getLastWeekMoods()

        // If no real data, return empty list or keep sample data for demo
        if (realMoods.isEmpty()) {
            return listOf(
                Entry(1f, 3f), // Monday - 😊
                Entry(2f, 4f), // Tuesday - 😄
                Entry(3f, 2f), // Wednesday - 😐
                Entry(4f, 5f), // Thursday - 🥳
                Entry(5f, 3f), // Friday - 😊
                Entry(6f, 1f), // Saturday - 😢
                Entry(7f, 4f)  // Sunday - 😄
            )
        }

        // Convert real mood entries to chart data
        val entries = mutableListOf<Entry>()
        realMoods.forEachIndexed { index, moodEntry ->
            // Convert emoji to number (1-5 scale)
            val moodValue = when (moodEntry.emoji) {
                "😢" -> 1f
                "😐" -> 2f
                "😊" -> 3f
                "😄" -> 4f
                "🥳" -> 5f
                else -> 3f // Default neutral
            }
            // Use index + 1 for X values (1-7 for days)
            entries.add(Entry((index + 1).toFloat(), moodValue))
        }

        return entries
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}