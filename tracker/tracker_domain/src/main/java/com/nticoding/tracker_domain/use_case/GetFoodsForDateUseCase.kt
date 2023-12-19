package com.nticoding.tracker_domain.use_case

import com.nticoding.tracker_domain.model.TrackableFood
import com.nticoding.tracker_domain.model.TrackedFood
import com.nticoding.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetFoodsForDateUseCase(
    private val repository: TrackerRepository
) {
    operator fun invoke(
        date: LocalDate,
    ): Flow<List<TrackedFood>> {

        return repository.getFoodsForDate(date)
    }
}