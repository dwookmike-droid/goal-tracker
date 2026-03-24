package com.goaltracker.app.domain

object SupportMessageGenerator {
    fun fromLegacy(completedCount: Int, latestLesson: String?): String {
        val cleanedLesson = latestLesson?.trim().orEmpty()
        return when {
            cleanedLesson.isNotEmpty() -> "최근 배운 점: $cleanedLesson"
            completedCount > 0 -> "너는 이미 ${completedCount}개의 프로젝트를 끝냈다."
            else -> "첫 프로젝트를 시작하고 너의 레거시를 쌓아라."
        }
    }
}
