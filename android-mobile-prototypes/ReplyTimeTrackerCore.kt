package com.example.replytimetracker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

/*
Sanitized core sample from my retained Reply Time Tracker source.
UI layout code is shortened here, but the state, timer, validation, parsing,
and duration logic follow the project I built in Android Studio.
*/

data class ReplyEntry(
    val id: Long,
    val contactName: String,
    val sentDateText: String,
    val sentTimeText: String,
    val replyDateText: String?,
    val replyTimeText: String?,
    val sentMillis: Long,
    val replyMillis: Long?
)

@Composable
fun ReplyTrackerStateExample() {
    val entries = remember { mutableStateListOf<ReplyEntry>() }

    var contactName by remember { mutableStateOf("Sample Contact") }
    var sentDateText by remember { mutableStateOf(formatTodayForInput()) }
    var sentTimeText by remember { mutableStateOf("") }
    var replyDateText by remember { mutableStateOf("") }
    var replyTimeText by remember { mutableStateOf("") }
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = System.currentTimeMillis()
            delay(1000)
        }
    }

    @Suppress("UNUSED_VARIABLE")
    val currentSnapshot = listOf(
        entries.size,
        contactName.length,
        sentDateText.length,
        sentTimeText.length,
        replyDateText.length,
        replyTimeText.length,
        currentTime
    )
}

fun parseDateTime(dateInput: String, timeInput: String): Long? {
    val date = parseDate(dateInput) ?: return null
    val time = parseTime(timeInput) ?: return null

    return date
        .atTime(time)
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}

fun parseDate(input: String): LocalDate? {
    val cleaned = input.trim()

    val formatters = listOf(
        DateTimeFormatter.ofPattern("M/d/uuuu", Locale.US),
        DateTimeFormatter.ofPattern("MM/dd/uuuu", Locale.US),
        DateTimeFormatter.ofPattern("M-d-uuuu", Locale.US),
        DateTimeFormatter.ofPattern("MM-dd-uuuu", Locale.US),
        DateTimeFormatter.ISO_LOCAL_DATE
    )

    for (formatter in formatters) {
        try {
            return LocalDate.parse(cleaned, formatter)
        } catch (_: DateTimeParseException) {
        }
    }

    return null
}

fun parseTime(input: String): LocalTime? {
    val cleaned = input
        .trim()
        .uppercase(Locale.US)
        .replace(".", "")

    val formatters = listOf(
        DateTimeFormatter.ofPattern("h:mm a", Locale.US),
        DateTimeFormatter.ofPattern("hh:mm a", Locale.US),
        DateTimeFormatter.ofPattern("h:mma", Locale.US),
        DateTimeFormatter.ofPattern("hh:mma", Locale.US),
        DateTimeFormatter.ofPattern("h a", Locale.US),
        DateTimeFormatter.ofPattern("ha", Locale.US),
        DateTimeFormatter.ofPattern("H:mm", Locale.US),
        DateTimeFormatter.ofPattern("HH:mm", Locale.US)
    )

    for (formatter in formatters) {
        try {
            return LocalTime.parse(cleaned, formatter)
        } catch (_: DateTimeParseException) {
        }
    }

    return null
}

fun formatTodayForInput(): String {
    return LocalDate.now().format(
        DateTimeFormatter.ofPattern("M/d/uuuu", Locale.US)
    )
}

fun formatDuration(milliseconds: Long): String {
    if (milliseconds < 0) return "Invalid time"

    val totalSeconds = milliseconds / 1000
    val totalMinutes = totalSeconds / 60
    val totalHours = totalMinutes / 60

    val days = totalHours / 24
    val hours = totalHours % 24
    val minutes = totalMinutes % 60
    val seconds = totalSeconds % 60

    return when {
        days > 0 -> "\${days}d \${hours}h \${minutes}m \${seconds}s"
        hours > 0 -> "\${hours}h \${minutes}m \${seconds}s"
        minutes > 0 -> "\${minutes}m \${seconds}s"
        else -> "\${seconds}s"
    }
}
