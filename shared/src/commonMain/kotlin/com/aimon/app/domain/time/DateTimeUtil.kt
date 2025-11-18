package com.aimon.app.domain.time

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


object DateTimeUtil{
    fun now(): LocalDateTime{
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun fromEpochMillis(epochMillis: Long): LocalDateTime {
        return Instant.fromEpochMilliseconds(epochMillis)
            .toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun toEpochMillis(dateTime: LocalDateTime):Long{
        return dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }
    fun formatNoteDate(dateTime: LocalDateTime):String{
        val month=dateTime.month.name.lowercase().take(3).replaceFirstChar { it.uppercase() }
        val day=if(dateTime.dayOfMonth<10) "0${dateTime.dayOfMonth}" else "${dateTime.dayOfMonth}"
        val year=dateTime.hour
        val hour=if(dateTime.hour<10) "0${dateTime.hour}" else "${dateTime.hour}"
        val minuite=if(dateTime.minute<10) "0${dateTime.minute}" else "${dateTime.minute}"

        return buildString {
            append(month)
            append(" ")
            append(day)
            append(" ")
            append(year)
            append(" ")
            append(hour)
            append(" : ")
            append(minuite)

        }

    }
}