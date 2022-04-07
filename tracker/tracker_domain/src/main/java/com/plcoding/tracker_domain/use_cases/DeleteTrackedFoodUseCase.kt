package com.plcoding.tracker_domain.use_cases

import com.plcoding.tracker_domain.models.TrackedFood
import com.plcoding.tracker_domain.repository.ITrackerRepository

class DeleteTrackedFoodUseCase(
    private val repository: ITrackerRepository
) {

    suspend operator fun invoke(
        trackedFood: TrackedFood
    ){

        repository.deleteTrackedFood(trackedFood)
    }
}