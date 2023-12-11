package com.nticoding.onboarding_domain.use_case

import com.nticoding.core.util.UIText
import com.nticoding.core.R

class ValidateNutrientsUseCase {

    operator fun invoke(
        carbRatioText: String,
        proteinRatioText: String,
        fatRatioText: String,
    ): Result {

        val carbsRatio = carbRatioText.toIntOrNull()
        val proteinRatio = proteinRatioText.toIntOrNull()
        val fatRatio = fatRatioText.toIntOrNull()

        if (carbsRatio == null || proteinRatio == null || fatRatio == null) {

            return Result.Error(
                message = UIText.StringResource(R.string.error_invalid_values)
            )
        }

        if (carbsRatio + proteinRatio + fatRatio != 100) {

            return Result.Error(
                message = UIText.StringResource(R.string.error_not_100_percent)
            )
        }

        return Result.Success(
            carbsRatio = carbsRatio / 100F,
            proteinRatio = proteinRatio / 100F,
            fatRatio = fatRatio / 100F,
        )
    }

    sealed class Result {
        data class Success(
            val carbsRatio: Float,
            val proteinRatio: Float,
            val fatRatio: Float,
        ): Result()

        data class Error(val message: UIText): Result()
    }
}