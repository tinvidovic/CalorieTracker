package com.plcoding.tracker_domain.use_cases

import com.plcoding.tracker_domain.models.MealType
import com.plcoding.tracker_domain.models.TrackableFood
import com.plcoding.tracker_domain.models.TrackedFood
import com.plcoding.tracker_domain.repository.ITrackerRepository
import java.time.LocalDate
import kotlin.math.roundToInt

class TrackFoodUseCase(
    private val repository: ITrackerRepository
)
{

    suspend operator fun invoke(
        food: TrackableFood,
        amount: Int,
        mealType: MealType,
        date: LocalDate
    ) {

        repository.insertTrackedFood(
            TrackedFood(
                name = food.name,
                carbs = ((food.carbsPer100g / 100f)*amount).roundToInt(),
                protein = ((food.proteinPer100g / 100f)*amount).roundToInt(),
                fat = ((food.fatsPer100g / 100f)*amount).roundToInt(),
                calories = ((food.caloriesPer100g / 100f)*amount).roundToInt(),
                imageUrl = food.imageUrl,
                mealType = mealType,
                amount = amount,
                date = date
                )
        )
    }
}