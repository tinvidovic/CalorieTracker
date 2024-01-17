package com.nticoding.tracker_domain.use_case

import com.google.common.truth.Truth.assertThat
import com.nticoding.core.domain.model.ActivityLevel
import com.nticoding.core.domain.model.Gender
import com.nticoding.core.domain.model.GoalType
import com.nticoding.core.domain.model.UserInfo
import com.nticoding.core.domain.preferences.Preferences
import com.nticoding.tracker_domain.model.MealType
import com.nticoding.tracker_domain.model.TrackedFood
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.random.Random

class CalculateMealNutrientsUseCaseTest {

    private lateinit var calculateMealNutrientsUseCase: CalculateMealNutrientsUseCase

    @Before
    fun setUp() {

        // Create mock preferences with no behaviour
        val preferences = mockk<Preferences>(relaxed = true)
        every { preferences.loadUserInfo() } returns UserInfo(
            gender = Gender.Male,
            age = 25,
            weight = 80f,
            height = 180,
            activityLevel = ActivityLevel.Medium,
            goalType = GoalType.KeepWeight,
            carbRatio = 0.4f,
            proteinRatio = 0.3f,
            fatRatio = 0.3f
        )

        // Re-init the class
        calculateMealNutrientsUseCase = CalculateMealNutrientsUseCase(preferences)
    }

    @Test
    fun `Breakfast calories properly calculated`() {

        val trackedFoods = (1..30).map {
            TrackedFood(
                name = "name",
                carbs = Random.nextInt(100),
                protein = Random.nextInt(100),
                fat = Random.nextInt(100),
                mealType = MealType.fromString(
                    listOf("breakfast", "lunch", "dinner", "snack").random()
                ),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now(),
                calories = Random.nextInt(2000)
            )
        }

        val result = calculateMealNutrientsUseCase(trackedFoods)

        val breakfastCalories = result.mealNutrients.values
            .filter {
                it.mealType is MealType.Breakfast
            }
            .sumOf { it.calories }

        val expectedCalories = trackedFoods
            .filter {
                it.mealType is MealType.Breakfast
            }
            .sumOf { it.calories }

        assertThat(breakfastCalories).isEqualTo(expectedCalories)
    }

    @Test
    fun `Lunch calories properly calculated`() {

        val trackedFoods = (1..30).map {
            TrackedFood(
                name = "name",
                carbs = Random.nextInt(100),
                protein = Random.nextInt(100),
                fat = Random.nextInt(100),
                mealType = MealType.fromString(
                    listOf("breakfast", "lunch", "dinner", "snack").random()
                ),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now(),
                calories = Random.nextInt(2000)
            )
        }

        val result = calculateMealNutrientsUseCase(trackedFoods)

        val lunchCalories = result.mealNutrients.values
            .filter {
                it.mealType is MealType.Lunch
            }
            .sumOf { it.calories }

        val expectedCalories = trackedFoods
            .filter {
                it.mealType is MealType.Lunch
            }
            .sumOf { it.calories }

        assertThat(lunchCalories).isEqualTo(expectedCalories)
    }

    @Test
    fun `Dinner calories properly calculated`() {

        val trackedFoods = (1..30).map {
            TrackedFood(
                name = "name",
                carbs = Random.nextInt(100),
                protein = Random.nextInt(100),
                fat = Random.nextInt(100),
                mealType = MealType.fromString(
                    listOf("breakfast", "lunch", "dinner", "snack").random()
                ),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now(),
                calories = Random.nextInt(2000)
            )
        }

        val result = calculateMealNutrientsUseCase(trackedFoods)

        val dinnerCalories = result.mealNutrients.values
            .filter {
                it.mealType is MealType.Dinner
            }
            .sumOf { it.calories }

        val expectedCalories = trackedFoods
            .filter {
                it.mealType is MealType.Dinner
            }
            .sumOf { it.calories }

        assertThat(dinnerCalories).isEqualTo(expectedCalories)
    }

    @Test
    fun `Snack calories properly calculated`() {

        val trackedFoods = (1..30).map {
            TrackedFood(
                name = "name",
                carbs = Random.nextInt(100),
                protein = Random.nextInt(100),
                fat = Random.nextInt(100),
                mealType = MealType.fromString(
                    listOf("breakfast", "lunch", "dinner", "snack").random()
                ),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now(),
                calories = Random.nextInt(2000)
            )
        }

        val result = calculateMealNutrientsUseCase(trackedFoods)

        val snackCalories = result.mealNutrients.values
            .filter {
                it.mealType is MealType.Snack
            }
            .sumOf { it.calories }

        val expectedCalories = trackedFoods
            .filter {
                it.mealType is MealType.Snack
            }
            .sumOf { it.calories }

        assertThat(snackCalories).isEqualTo(expectedCalories)
    }
}