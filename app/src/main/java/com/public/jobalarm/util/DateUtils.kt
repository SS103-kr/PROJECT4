package com.jobalarm.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object DateUtils {

    private val YYYYMMDD: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    private val DISPLAY: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

    fun parse(yyyymmdd: String?): LocalDate? {
        if (yyyymmdd.isNullOrBlank() || yyyymmdd == "-") return null
        val trimmed = yyyymmdd.trim()
        if (trimmed.length != 8 || !trimmed.all { it.isDigit() }) return null
        return try {
            LocalDate.parse(trimmed, YYYYMMDD)
        } catch (_: Exception) {
            null
        }
    }

    fun formatDisplay(yyyymmdd: String?): String {
        val d = parse(yyyymmdd) ?: return yyyymmdd.orEmpty()
        return d.format(DISPLAY)
    }

    fun dDay(yyyymmdd: String?, today: LocalDate = LocalDate.now()): Int? {
        val d = parse(yyyymmdd) ?: return null
        return ChronoUnit.DAYS.between(today, d).toInt()
    }

    fun dDayLabel(yyyymmdd: String?): String {
        val n = dDay(yyyymmdd) ?: return "-"
        return when {
            n > 0 -> "D-$n"
            n == 0 -> "D-DAY"
            else -> "마감"
        }
    }
}
