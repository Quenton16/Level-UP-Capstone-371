package com.example.levelup.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val description: String,
    val goalPerDay: Int,
    val icon: String,
    val streak: Int = 0,
    val level: Int = 1,
    val xp: Int = 0,
    val todayCount: Int = 0
) {
    fun toHabit(): Habit = Habit(
        id = id,
        name = name,
        description = description,
        goalPerDay = goalPerDay,
        icon = icon,
        streak = streak,
        level = level,
        xp = xp,
        todayCount = todayCount
    )
}

fun Habit.toEntity(): HabitEntity = HabitEntity(
    id = id,
    name = name,
    description = description,
    goalPerDay = goalPerDay,
    icon = icon,
    streak = streak,
    level = level,
    xp = xp,
    todayCount = todayCount
)
