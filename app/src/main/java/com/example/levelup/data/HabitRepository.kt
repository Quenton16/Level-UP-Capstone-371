package com.example.levelup.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.time.LocalDate

class HabitRepository {

    // In-memory habit list for now
    private val _habits: SnapshotStateList<Habit> = mutableStateListOf(
        Habit(
            id = 1,
            name = "Study 30 minutes",
            description = "Work on classes or coding for at least 30 minutes",
            goalPerDay = 1,
            icon = "📚"
        ),
        Habit(
            id = 2,
            name = "Drink Water",
            description = "Drink a full bottle of water",
            goalPerDay = 3,
            icon = "💧"
        )
    )

    val habits: SnapshotStateList<Habit> = _habits

    private var nextId = 3

    fun addHabit(name: String, description: String, goalPerDay: Int, icon: String) {
        _habits.add(
            Habit(
                id = nextId++,
                name = name,
                description = description,
                goalPerDay = goalPerDay,
                icon = icon
            )
        )
    }

    fun updateHabit(updated: Habit) {
        val index = _habits.indexOfFirst { it.id == updated.id }
        if (index != -1) {
            _habits[index] = updated
        }
    }

    fun completeHabitToday(habitId: Int) {
        val index = _habits.indexOfFirst { it.id == habitId }
        if (index == -1) return

        val habit = _habits[index]
        val today = LocalDate.now()

        val isNewDay = habit.lastCompletedDate != today
        val newTodayCount = if (isNewDay) 1 else habit.todayCount + 1

        // Check if user hit goal for today
        val hitGoalNow = newTodayCount >= habit.goalPerDay &&
                (habit.lastCompletedDate != today || habit.todayCount < habit.goalPerDay)

        val newStreak = when {
            hitGoalNow && habit.lastCompletedDate == today.minusDays(1) -> habit.streak + 1
            hitGoalNow && habit.streak == 0 -> 1
            hitGoalNow -> 1 // restart streak if broken
            else -> habit.streak
        }

        // XP: 10 per completion + bonus when hitting daily goal
        val gainedXp = 10 + if (hitGoalNow) 20 else 0
        val totalXp = habit.xp + gainedXp
        val newLevel = 1 + (totalXp / 100)  // 100 XP per level for now

        _habits[index] = habit.copy(
            todayCount = newTodayCount,
            lastCompletedDate = today,
            streak = newStreak,
            xp = totalXp,
            level = newLevel
        )
    }

    fun getCompletionRate(): Float {
        if (_habits.isEmpty()) return 0f
        val completedToday = _habits.count { it.todayCount >= it.goalPerDay }
        return completedToday.toFloat() / _habits.size
    }
}
