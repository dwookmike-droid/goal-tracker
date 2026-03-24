package com.goaltracker.app.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class SupportMessageGeneratorTest {

    @Test
    fun returns_count_message_when_user_has_completed_projects() {
        val result = SupportMessageGenerator.fromLegacy(
            completedCount = 3,
            latestLesson = null,
        )

        assertEquals("You already finished 3 projects.", result)
    }

    @Test
    fun returns_latest_lesson_when_available() {
        val result = SupportMessageGenerator.fromLegacy(
            completedCount = 1,
            latestLesson = "Ship before it feels perfect.",
        )

        assertEquals("Last lesson: Ship before it feels perfect.", result)
    }

    @Test
    fun returns_default_message_when_no_legacy_exists() {
        val result = SupportMessageGenerator.fromLegacy(
            completedCount = 0,
            latestLesson = null,
        )

        assertEquals("Start your first project and build your legacy.", result)
    }
}
