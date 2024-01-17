package com.nticoding.tracker_data.repository

import com.nticoding.tracker_data.local.TrackerDao
import com.nticoding.tracker_data.mapper.toTrackableFood
import com.nticoding.tracker_data.mapper.toTrackedFood
import com.nticoding.tracker_data.mapper.toTrackedFoodEntity
import com.nticoding.tracker_data.remote.OpenFoodApi
import com.nticoding.tracker_domain.model.TrackableFood
import com.nticoding.tracker_domain.model.TrackedFood
import com.nticoding.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class TrackerRepository(
    private val dao: TrackerDao,
    private val api: OpenFoodApi
): TrackerRepository {
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

            Result.success(
                searchDto.products
                    .filter {
                        val calculatedCalories = it.nutriments.carbohydrates100g * 4f +
                                it.nutriments.proteins100g * 4f +
                                it.nutriments.fat100g * 9f

                        val lb = calculatedCalories * 0.99f
                        val ub = calculatedCalories * 1.01f

                        it.nutriments.energyKcal100g in (lb..ub)
                    }
                    .mapNotNull {
                    it.toTrackableFood()
                }
            )
        } catch(e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun insertTrackedFood(food: TrackedFood) {
        dao.insertTrackedFood(food.toTrackedFoodEntity())
    }

    override suspend fun deleteTrackedFood(food: TrackedFood) {
        dao.deleteTrackedFood(food.toTrackedFoodEntity())
    }

    override fun getFoodsForDate(localDate: LocalDate): Flow<List<TrackedFood>> {

        return dao.getFoodsForData(
            day = localDate.dayOfMonth,
            month = localDate.monthValue,
            year = localDate.year
        ).map { entities ->
            entities.map {
                it.toTrackedFood()
            }
        }
    }
}