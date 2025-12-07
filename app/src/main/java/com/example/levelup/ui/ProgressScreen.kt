package com.example.levelup.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.levelup.data.HabitRepository
import kotlin.math.roundToInt

@Composable
fun ProgressScreen(
    repository: HabitRepository
) {
    val completionRate = repository.getCompletionRate()
    val percentage = (completionRate * 100).roundToInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Progress",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Today's Completion",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "$percentage%",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Weekly and monthly charts can go here.",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "For the project, you can use a simple bar chart\n" +
                    "or multiple rows showing % for each habit.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
