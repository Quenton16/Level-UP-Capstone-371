package com.example.levelup.data

import java.time.LocalDate

data class Habit(
    val id: Int,
    val name: String,
    val description: String,
    val goalPerDay: Int,        // e.g. 1 time, 3 times a day
    val icon: String,           // simple emoji for now
    val streak: Int = 0,
    val level: Int = 1,
    val xp: Int = 0,
    val lastCompletedDate: LocalDate? = null,
    val todayCount: Int = 0
)
