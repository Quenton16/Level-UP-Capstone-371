package com.example.levelup.data

data class Habit(
    val id: Long = 0L,
    val name: String,
    val description: String,
    val goalPerDay: Int,
    val icon: String,
    val streak: Int = 0,
    val level: Int = 1,
    val xp: Int = 0,
    val todayCount: Int = 0
)
