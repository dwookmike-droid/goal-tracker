package com.goaltracker.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.goaltracker.app.data.model.LegacyRecordEntity
import com.goaltracker.app.data.model.MilestoneEntity
import com.goaltracker.app.data.model.ProjectEntity

@Database(
    entities = [ProjectEntity::class, MilestoneEntity::class, LegacyRecordEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class GoalTrackerDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao

    companion object {
        @Volatile
        private var INSTANCE: GoalTrackerDatabase? = null

        fun getInstance(context: Context): GoalTrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    GoalTrackerDatabase::class.java,
                    "goal-tracker.db",
                ).build().also { INSTANCE = it }
            }
        }
    }
}
