package com.plcoding.tracker_data.repository

import com.plcoding.tracker_data.local.ITrackerDAO
import com.plcoding.tracker_data.mapper.toTrackableFood
import com.plcoding.tracker_data.mapper.toTrackedFood
import com.plcoding.tracker_data.mapper.toTrackedFoodEntity
import com.plcoding.tracker_data.remote.placeholder.IOpenFoodAPI
import com.plcoding.tracker_domain.models.TrackableFood
import com.plcoding.tracker_domain.models.TrackedFood
import com.plcoding.tracker_domain.repository.ITrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class TrackerRepository(
    private val dao: ITrackerDAO,
    private val api: IOpenFoodAPI
) : ITrackerRepository {

    override suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>> {

        return try {

            val searchDto = api.searchFood(
                query = query,
                page = page,
                pageSize = pageSize
            )

            Result.success(searchDto.products.mapNotNull { it.toTrackableFood() })
        } catch (e: Exception){
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun insertTrackedFood(food: TrackedFood) {

        food.toTrackedFoodEntity()?.let { dao.insertTrackedFood(it) }
    }

    override suspend fun deleteTrackedFood(food: TrackedFood) {

        food.toTrackedFoodEntity()?.let { dao.deleteTrackedFood(it) }
    }

    override fun getFoodsForDate(localDate: LocalDate): Flow<List<TrackedFood>> {

        return dao.getFoodsForDate(
            day = localDate.dayOfMonth,
            month = localDate.monthValue,
            year = localDate.year
        ).map { entities ->

            entities.map { it.toTrackedFood() }
        }
    }
}