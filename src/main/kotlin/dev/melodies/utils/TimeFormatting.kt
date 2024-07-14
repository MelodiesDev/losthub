package dev.melodies.utils

import org.apache.commons.lang.time.DurationFormatUtils

object TimeFormatting {
    fun formatRemainingDuration(startedAt: Long, duration: Long): String {
        val now = System.currentTimeMillis()
        val timeSinceStart = now - startedAt
        val countdown = duration - timeSinceStart
        return DurationFormatUtils.formatDurationWords(countdown, true, true)
    }
}