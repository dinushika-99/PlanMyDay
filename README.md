# 🌱 Daily Habit & Mood Tracker App
A simple and user-friendly Android application built with **Kotlin** for tracking daily habits, monitoring mood patterns, and staying hydrated through personalized reminders.

---

## 📌 Features

### ✅ Habit Tracking
- Add new habits
- Edit existing habits
- Delete habits
- View all habits in a scrollable list (RecyclerView)

### 😊 Mood Tracking
- Record your daily mood
- Visual mood chart to view emotional patterns over time
- Mood history stored persistently

### 💧 Hydration Reminder
- Reminders at **1 hour, 2 hours, or 3 hours** intervals
- Customizable in Settings
- Notifications to stay hydrated

### 🎨 Themes & Settings
- Switch app themes
- Configure hydration frequency
- All settings stored persistently

---

## 🧩 Technical Overview

### ✔ Built Using **Kotlin**
Modern Android development practices.

### ✔ Architecture
- **MVVM (ViewModel)** for managing UI-related data
- ViewModel ensures data survives configuration changes (e.g., screen rotation)

### ✔ Navigation
- **Fragments** used for navigation between:
    - Dashboard
    - Habits
    - Moods
    - Settings

### ✔ RecyclerView
Used for habits and mood lists with:
- Custom Adapters
- ViewHolders
- Linear Layout Managers

### ✔ Intents
- **Explicit Intents** for moving between activities
- Handling data passing where required

### ✔ Data Persistence
- Local storage for:
    - Habits
    - Moods
    - Hydration settings
    - Theme preferences

### ✔ UI / UX
- Clean and responsive design
- Smooth navigation between all app sections

---

## 📱 Screens (Optional)
_Add screenshots here later if you have them._

---

## 🛠 Tools Used
- Android Studio
- Kotlin
- LiveData / ViewModel
- RecyclerView
- XML Layouts
- SharedPreferences (or indicate if using Room later)

---

## 📦 How to Run the Project
1. Clone the repository
2. Open project in **Android Studio**
3. Sync Gradle
4. Run on emulator or physical device

---

## 👩‍💻 Author
**Nimanthi Weerakoon**  
IT Undergraduate | Android & Data Science Enthusiast

---

