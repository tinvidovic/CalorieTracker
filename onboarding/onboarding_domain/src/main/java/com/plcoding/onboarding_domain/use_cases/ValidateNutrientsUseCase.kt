package com.plcoding.onboarding_domain.use_cases

import com.plcoding.core.util.UiText
import com.plcoding.onboarding_domain.R

class ValidateNutrientsUseCase {

    operator fun invoke(
        carbRatioText: String,
        proteinRatioText: String,
        fatRatioText: String,
    ): Result {

        val carbRatio = carbRatioText.toIntOrNull()
        val proteinRatio = proteinRatioText.toIntOrNull()
        val fatRatio = fatRatioText.toIntOrNull()

        // Check if everything is entered
        if (carbRatio == null || proteinRatio == null || fatRatio == null){
            return Result.Error(
                message = UiText.StringResource(R.string.error_invalid_values)
            )
        }

        if (carbRatio + proteinRatio + fatRatio != 100){
            return Result.Error(
                message = UiText.StringResource(R.string.error_not_100_percent)
            )
        }

        return Result.Success(
            carbRatio = carbRatio/100f,
            proteinRatio = proteinRatio/100f,
            fatRatio = fatRatio/100f
        )
    }

    sealed class Result {

        data class Success(
            val carbRatio: Float,
            val proteinRatio: Float,
            val fatRatio: Float
        ) : Result()

        data class Error(
            val message: UiText
        ) : Result()
    }
}