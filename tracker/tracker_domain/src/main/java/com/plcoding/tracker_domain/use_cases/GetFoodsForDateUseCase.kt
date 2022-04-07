package com.plcoding.tracker_domain.use_cases

import com.plcoding.tracker_domain.models.TrackedFood
import com.plcoding.tracker_domain.repository.ITrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import java.time.LocalDate

class GetFoodsForDateUseCase(
    private val repository: ITrackerRepository
) {

    operator fun invoke(
        date: LocalDate
    ): Flow<List<TrackedFood>> {

        return  repository.getFoodsForDate(date)
    }
}