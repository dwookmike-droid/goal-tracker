package com.goaltracker.app.data.model

data class LegacyListItem(
    val id: Long,
    val projectId: Long,
    val projectTitle: String,
    val achievement: String,
    val lesson: String,
    val createdAt: Long,
)
