package com.app.partyzone.core.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

fun LocalDate.Companion.now(): LocalDate {
    return Clock.System.todayIn(TimeZone.currentSystemDefault())
}

fun LocalTime.Companion.now(): LocalTime {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
}

fun String.isEmptyOrBlank(): Boolean {
    return this.isEmpty() || this.isBlank()
}

fun String.isNotEmptyAndBlank(): Boolean {
    return this.isNotEmpty() && this.isNotBlank()
}