package com.nticoding.tracker_domain.di

import com.nticoding.core.domain.preferences.Preferences
import com.nticoding.tracker_domain.repository.TrackerRepository
import com.nticoding.tracker_domain.use_case.CalculateMealNutrientsUseCase
import com.nticoding.tracker_domain.use_case.DeleteTrackedFoodUseCase
import com.nticoding.tracker_domain.use_case.GetFoodsForDateUseCase
import com.nticoding.tracker_domain.use_case.SearchFoodUseCase
import com.nticoding.tracker_domain.use_case.TrackFoodUseCase
import com.nticoding.tracker_domain.use_case.TrackerUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object TrackerDomainModule {

    @ViewModelScoped
    @Provides
    fun provideTrackerUserCases(
        repository: TrackerRepository,
        preferences: Preferences,
    ): TrackerUseCases {

        return TrackerUseCases(
            trackFoodUseCase = TrackFoodUseCase(repository),
            searchFoodUseCase = SearchFoodUseCase(repository),
            getFoodsForDateUseCase = GetFoodsForDateUseCase(repository),
            deleteTrackedFoodUseCase = DeleteTrackedFoodUseCase(repository),
            calculateMealNutrientsUseCase = CalculateMealNutrientsUseCase(preferences),
        )
    }
}