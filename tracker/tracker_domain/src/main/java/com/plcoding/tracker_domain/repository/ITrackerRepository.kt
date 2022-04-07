package com.plcoding.tracker_domain.repository

import com.plcoding.tracker_domain.models.TrackableFood
import com.plcoding.tracker_domain.models.TrackedFood
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ITrackerRepository {

    suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>>

    suspend fun insertTrackedFood(food: TrackedFood)

    suspend fun deleteTrackedFood(food: TrackedFood)

    fun getFoodsForDate(localDate: LocalDate) : Flow<List<TrackedFood>>
}