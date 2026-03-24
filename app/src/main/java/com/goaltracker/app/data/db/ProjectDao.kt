package com.goaltracker.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.goaltracker.app.data.model.LegacyRecordEntity
import com.goaltracker.app.data.model.LegacyListItem
import com.goaltracker.app.data.model.MilestoneEntity
import com.goaltracker.app.data.model.ProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects WHERE status = 'ACTIVE' ORDER BY isFocus DESC, updatedAt DESC")
    fun observeActiveProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE status = 'COMPLETED' ORDER BY completedAt DESC")
    fun observeCompletedProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE isFocus = 1 LIMIT 1")
    fun observeFocusProject(): Flow<ProjectEntity?>

    @Query("SELECT * FROM projects WHERE isFocus = 1 LIMIT 1")
    suspend fun getFocusProjectNow(): ProjectEntity?

    @Query("SELECT * FROM projects WHERE id = :projectId LIMIT 1")
    fun observeProject(projectId: Long): Flow<ProjectEntity?>

    @Query("SELECT * FROM milestones WHERE projectId = :projectId ORDER BY createdAt ASC")
    fun observeMilestones(projectId: Long): Flow<List<MilestoneEntity>>

    @Query("SELECT * FROM legacy_records ORDER BY createdAt DESC")
    fun observeLegacyRecords(): Flow<List<LegacyRecordEntity>>

    @Query(
        """
        SELECT legacy_records.id AS id,
               legacy_records.projectId AS projectId,
               projects.title AS projectTitle,
               legacy_records.achievement AS achievement,
               legacy_records.lesson AS lesson,
               legacy_records.createdAt AS createdAt
        FROM legacy_records
        INNER JOIN projects ON projects.id = legacy_records.projectId
        ORDER BY legacy_records.createdAt DESC
        """
    )
    fun observeLegacyListItems(): Flow<List<LegacyListItem>>

    @Query("SELECT COUNT(*) FROM projects WHERE status = 'COMPLETED'")
    fun observeCompletedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM projects WHERE status = 'COMPLETED'")
    suspend fun getCompletedCountNow(): Int

    @Query("SELECT * FROM legacy_records ORDER BY createdAt DESC LIMIT 1")
    fun observeLatestLegacy(): Flow<LegacyRecordEntity?>

    @Query("SELECT * FROM legacy_records ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLatestLegacyNow(): LegacyRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity): Long

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Query("UPDATE projects SET isFocus = 0")
    suspend fun clearFocus()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestone(milestone: MilestoneEntity): Long

    @Update
    suspend fun updateMilestone(milestone: MilestoneEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLegacyRecord(record: LegacyRecordEntity): Long
}
