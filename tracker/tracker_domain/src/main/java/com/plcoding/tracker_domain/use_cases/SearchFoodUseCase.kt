package com.plcoding.tracker_domain.use_cases

import com.plcoding.tracker_domain.models.TrackableFood
import com.plcoding.tracker_domain.repository.ITrackerRepository

class SearchFoodUseCase(
    private val repository: ITrackerRepository
) {

    suspend operator fun invoke(
        query: String,
        page: Int = 1,
        pageSize: Int = 40
    ) : Result<List<TrackableFood>> {

        if (query.isBlank()){
            return Result.success(emptyList())
        }else {

            return repository.searchFood(query.trim(), page, pageSize)
        }
    }
}