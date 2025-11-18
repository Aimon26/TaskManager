package com.aimon.app.domain.task

import com.aimon.app.presentation.BabyBlueHex
import com.aimon.app.presentation.CoralHex
import com.aimon.app.presentation.LightGreenHex
import com.aimon.app.presentation.MintHex
import com.aimon.app.presentation.PeachHex
import com.aimon.app.presentation.RedOrangeHex
import com.aimon.app.presentation.RedPinkHex
import com.aimon.app.presentation.SoftOrangeHex
import com.aimon.app.presentation.SoftYellowHex
import com.aimon.app.presentation.VioletHex
import kotlinx.datetime.LocalDateTime

data class Task(
    val id: Long = 0L,
    val title: String,
    val description: String? = null,
    val priority: Priority = Priority.MEDIUM,
    val status: Status = Status.TO_DO,
    val dueDate: LocalDateTime ,
    val created: LocalDateTime,
    val updated: LocalDateTime,
    val colorHex: Long
) {
    companion object {
        private val colors = listOf(
            RedOrangeHex, RedPinkHex, LightGreenHex, BabyBlueHex, VioletHex,
            SoftOrangeHex, SoftYellowHex, PeachHex, CoralHex, MintHex
        )

        fun generateRandomColor() = colors.random()
    }
}

enum class Priority {
    LOW, MEDIUM, HIGH;

    companion object {
        fun fromString(value: String): Priority {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: MEDIUM
        }
    }
}

enum class Status {
    TO_DO,
    IN_PROGRESS,
    DONE;

    companion object {
        fun fromDisplayString(value: String): Status = when (value) {
            "To Do" -> TO_DO
            "In Progress" -> IN_PROGRESS
            "Done" -> DONE
            else -> TO_DO
        }
    }
}