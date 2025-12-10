package com.example.levelup.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.math.max

class HabitRepository(
    private val dao: HabitDao
) {
    // Background scope for DB + any background work
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    //  UI, backed by Room
    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits.asStateFlow()

    init {
        // Keep _habits in sync with the Room database
        scope.launch {
            dao.getAllHabits()
                .map { entities -> entities.map { it.toHabit() } }
                .collect { list ->
                    _habits.value = list
                }
        }
    }


    fun getCompletionRate(): Float {
        val list = _habits.value
        if (list.isEmpty()) return 0f
        val completedToday = list.count { it.todayCount >= it.goalPerDay }
        return completedToday.toFloat() / list.size
    }


    fun addHabit(name: String, description: String, goalPerDay: Int, icon: String) {
        scope.launch {
            val entity = Habit(
                name = name,
                description = description,
                goalPerDay = goalPerDay,
                icon = icon
            ).toEntity()
            dao.insertHabit(entity)
        }
    }


    fun completeHabitOnce(habitId: Long) {
        scope.launch {
            val entity = dao.getHabitById(habitId) ?: return@launch

            val newTodayCount = entity.todayCount + 1
            val hitGoalNow = newTodayCount >= entity.goalPerDay &&
                    entity.todayCount < entity.goalPerDay

            val newStreak = if (hitGoalNow) entity.streak + 1 else entity.streak

            // XP: 10 per completion + 20 bonus when hitting daily goal
            val gainedXp = 10 + if (hitGoalNow) 20 else 0
            val totalXp = entity.xp + gainedXp
            val newLevel = max(1, 1 + (totalXp / 100))

            val updated = entity.copy(
                todayCount = newTodayCount,
                streak = newStreak,
                xp = totalXp,
                level = newLevel
            )

            dao.updateHabit(updated)
        }
    }
}
