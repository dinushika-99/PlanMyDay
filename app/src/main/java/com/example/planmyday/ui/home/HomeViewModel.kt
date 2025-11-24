import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planmyday.data.MoodRepository
import com.example.planmyday.model.MoodEntry
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val moodRepository: MoodRepository) : ViewModel() {

    // Holds the state of the mood chart data
    private val _moodChartData = MutableStateFlow<List<Entry>>(emptyList())
    val moodChartData: StateFlow<List<Entry>> = _moodChartData

    // Load data from the repository
    fun loadLastWeekMoodData() {
        viewModelScope.launch {
            val moods = fetchMoodsFromRepository()    //call your MoodRepository
            val chartEntries = convertMoodsToChartEntries(moods)
            _moodChartData.value = chartEntries
        }
    }

    // For example, if your repository returns a Flow:
    // return moodRepository.getLastWeekMoods().first()
    // Or if it's a suspend function:
    // return moodRepository.getLastWeekMoods()
    private suspend fun fetchMoodsFromRepository(): List<MoodEntry> {
        return emptyList() // Placeholder
    }

    private fun convertMoodsToChartEntries(moods: List<MoodEntry>): List<Entry> {
        val entries = mutableListOf<Entry>()
        // Example conversion logic. Adjust based on your MoodEntry structure.
        moods.forEachIndexed { index, moodEntry ->
            // You need to map your mood value (e.g., a number or emoji) to a float for the Y-axis.
            // You also need to assign an X value (e.g., day index).
            val yValue = mapMoodToFloat(moodEntry.emoji)
            entries.add(Entry(index.toFloat(), yValue))
        }
        return entries
    }

    private fun mapMoodToFloat(moodValue: String): Float {
        // Example mapping: Convert emojis or mood strings to a numerical scale.
        return when (moodValue) {
            "😢", "Sad" -> 1f
            "😐", "Neutral" -> 2f
            "😊", "Happy" -> 3f
            "😄", "Very Happy" -> 4f
            "🥳", "Excited" -> 5f
            else -> 0f
        }
    }
}