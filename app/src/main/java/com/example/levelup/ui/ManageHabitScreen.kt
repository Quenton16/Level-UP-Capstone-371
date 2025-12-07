package com.example.levelup.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.levelup.data.HabitRepository

@Composable
fun ManageHabitScreen(
    repository: HabitRepository
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var goalPerDayText by remember { mutableStateOf("1") }
    var icon by remember { mutableStateOf("⭐") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Manage Habits",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Habit Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = goalPerDayText,
            onValueChange = { goalPerDayText = it.filter { c -> c.isDigit() } },
            label = { Text("Times per day (goal)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = icon,
            onValueChange = { icon = it.take(2) }, // keep it simple
            label = { Text("Icon (emoji)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val goal = goalPerDayText.toIntOrNull() ?: 1
                if (name.isBlank()) {
                    message = "Please enter a habit name."
                } else {
                    repository.addHabit(
                        name = name.trim(),
                        description = description.trim(),
                        goalPerDay = goal.coerceAtLeast(1),
                        icon = if (icon.isBlank()) "⭐" else icon
                    )
                    message = "Habit added!"
                    name = ""
                    description = ""
                    goalPerDayText = "1"
                    icon = "⭐"
                }
            }
        ) {
            Text("Add Habit")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (message.isNotEmpty()) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
