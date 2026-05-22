package com.jobalarm

import com.jobalarm.util.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class DateUtilsTest {
    @Test
    fun parse_validDate_returnsLocalDate() {
        val d = DateUtils.parse("20260423")
        assertEquals(LocalDate.of(2026, 4, 23), d)
    }

    @Test
    fun parse_blank_returnsNull() {
        assertNull(DateUtils.parse(null))
        assertNull(DateUtils.parse(""))
        assertNull(DateUtils.parse("-"))
    }

    @Test
    fun dDay_futureDate_returnsPositive() {
        val today = LocalDate.of(2026, 4, 20)
        assertEquals(3, DateUtils.dDay("20260423", today))
    }
}
