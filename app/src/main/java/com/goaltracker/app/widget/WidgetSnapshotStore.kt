package com.goaltracker.app.widget

import android.content.Context

data class WidgetSnapshot(
    val title: String,
    val progress: Int,
    val supportMessage: String,
)

class WidgetSnapshotStore(context: Context) {
    private val prefs = context.getSharedPreferences("goal_tracker_widget", Context.MODE_PRIVATE)

    fun write(snapshot: WidgetSnapshot) {
        prefs.edit()
            .putString(KEY_TITLE, snapshot.title)
            .putInt(KEY_PROGRESS, snapshot.progress)
            .putString(KEY_SUPPORT, snapshot.supportMessage)
            .apply()
    }

    fun read(): WidgetSnapshot {
        return WidgetSnapshot(
            title = prefs.getString(KEY_TITLE, "No focus project") ?: "No focus project",
            progress = prefs.getInt(KEY_PROGRESS, 0),
            supportMessage = prefs.getString(KEY_SUPPORT, "Create a project to get started.") ?: "Create a project to get started.",
        )
    }

    companion object {
        private const val KEY_TITLE = "title"
        private const val KEY_PROGRESS = "progress"
        private const val KEY_SUPPORT = "support"
    }
}
