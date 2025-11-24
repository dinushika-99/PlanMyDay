package com.example.planmyday.ui.habit

import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planmyday.R

      //initialize the views that we created in the list_item_layout.xml

class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textHabit: TextView = itemView.findViewById(R.id.textHabit)
    val btnLike: CheckBox = itemView.findViewById(R.id.btnLike)
    val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit) // Add this line
    val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete) // Add this line
}