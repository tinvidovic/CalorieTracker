package com.nticoding.tracker_domain.use_case

import com.nticoding.tracker_domain.model.TrackableFood
import com.nticoding.tracker_domain.repository.TrackerRepository

class SearchFoodUseCase(
    private val repository: TrackerRepository
) {

    suspend operator fun invoke(
        query: String,
        page: Int = 1,
        pageSize: Int = 40,
    ): Result<List<TrackableFood>> {

        return if (query.isNotBlank()) {

            repository.searchFood(
                query = query.trim(),
                page = page,
                pageSize = page
            )
        } else
            Result.success(emptyList())
    }
}