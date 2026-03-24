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

        assertEquals("너는 이미 3개의 프로젝트를 끝냈다.", result)
    }

    @Test
    fun returns_latest_lesson_when_available() {
        val result = SupportMessageGenerator.fromLegacy(
            completedCount = 1,
            latestLesson = "Ship before it feels perfect.",
        )

        assertEquals("최근 배운 점: Ship before it feels perfect.", result)
    }

    @Test
    fun returns_default_message_when_no_legacy_exists() {
        val result = SupportMessageGenerator.fromLegacy(
            completedCount = 0,
            latestLesson = null,
        )

        assertEquals("첫 프로젝트를 시작하고 너의 레거시를 쌓아라.", result)
    }
}
