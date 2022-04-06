package com.plcoding.tracker_data.local

import androidx.room.*
import com.plcoding.tracker_data.local.entity.TrackedFoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ITrackerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackedFood(trackedFoodEntity: TrackedFoodEntity)

    @Delete
    suspend fun deleteTrackedFood(trackedFoodEntity: TrackedFoodEntity)

    @Query(
        """
            SELECT *
            FROM TrackedFoodEntity
            WHERE dayOfMonth = :day AND month = :month AND year = :year
        """
    )
    fun getFoodsForDate(day: Int, month: Int, year: Int): Flow<List<TrackedFoodEntity>>
}