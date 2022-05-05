package com.imzqqq.app.core.resources

import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

object DateProvider {

    private val zoneId = ZoneId.systemDefault()
    private val zoneOffset by lazy {
        val now = currentLocalDateTime()
        zoneId.rules.getOffset(now)
    }

    fun toLocalDateTime(timestamp: Long?): LocalDateTime {
        val instant = Instant.ofEpochMilli(timestamp ?: 0)
        return LocalDateTime.ofInstant(instant, zoneId)
    }

    fun currentLocalDateTime(): LocalDateTime {
        val instant = Instant.now()
        return LocalDateTime.ofInstant(instant, zoneId)
    }

    fun toTimestamp(localDateTime: LocalDateTime): Long {
        return localDateTime.toInstant(zoneOffset).toEpochMilli()
    }
}

fun LocalDateTime.toTimestamp(): Long = DateProvider.toTimestamp(this)
