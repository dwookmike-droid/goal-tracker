package com.goaltracker.app.domain

object SupportMessageGenerator {
    fun fromLegacy(completedCount: Int, latestLesson: String?): String {
        val cleanedLesson = latestLesson?.trim().orEmpty()
        return when {
            cleanedLesson.isNotEmpty() -> "Last lesson: $cleanedLesson"
            completedCount > 0 -> "You already finished $completedCount projects."
            else -> "Start your first project and build your legacy."
        }
    }
}
