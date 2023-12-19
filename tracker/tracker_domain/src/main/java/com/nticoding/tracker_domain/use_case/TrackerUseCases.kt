package com.nticoding.tracker_domain.use_case

data class TrackerUseCases(
    val trackFoodUseCase: TrackFoodUseCase,
    val searchFoodUseCase: SearchFoodUseCase,
    val getFoodsForDateUseCase: GetFoodsForDateUseCase,
    val deleteTrackedFoodUseCase: DeleteTrackedFoodUseCase,
    val calculateMealNutrientsUseCase: CalculateMealNutrientsUseCase,
)
