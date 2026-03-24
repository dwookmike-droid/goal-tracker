package com.goaltracker.app.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.goaltracker.app.R
import com.goaltracker.app.ui.MainActivity

class FocusProjectWidgetReceiver : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        appWidgetIds.forEach { appWidgetId ->
            val views = buildViews(context)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    companion object {
        fun updateAll(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, FocusProjectWidgetReceiver::class.java)
            val ids = appWidgetManager.getAppWidgetIds(componentName)
            if (ids.isNotEmpty()) {
                val views = buildViews(context)
                appWidgetManager.updateAppWidget(ids, views)
            }
        }

        private fun buildViews(context: Context): RemoteViews {
            val snapshot = WidgetSnapshotStore(context).read()
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            return RemoteViews(context.packageName, R.layout.widget_focus_project).apply {
                setTextViewText(R.id.widget_title, snapshot.title)
                setTextViewText(R.id.widget_progress, "Progress ${snapshot.progress}%")
                setTextViewText(R.id.widget_support, snapshot.supportMessage)
                setOnClickPendingIntent(R.id.widget_root, pendingIntent)
            }
        }
    }
}
