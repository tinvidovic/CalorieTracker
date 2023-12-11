package com.nticoding.onboarding_presentation.nutrient_goal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nticoding.core.domain.preferences.Preferences
import com.nticoding.core.domain.use_case.FilterOutDigitsUseCase
import com.nticoding.core.navigation.Route
import com.nticoding.core.util.UIEvent
import com.nticoding.onboarding_domain.use_case.ValidateNutrientsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutrientGoalViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterOutDigitsUseCase: FilterOutDigitsUseCase,
    private val validateNutrientsUseCase: ValidateNutrientsUseCase,
): ViewModel() {

    var state by mutableStateOf(NutrientGoalState())
        private set

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: NutrientGoalEvent) {

        when(event) {
            is NutrientGoalEvent.OnCarbRatioEnter -> {
                state = state.copy(
                    carbRatio = filterOutDigitsUseCase(event.ratio)
                )
            }
            is NutrientGoalEvent.OnProteinRatioEnter -> {
                state = state.copy(
                    proteinRatio = filterOutDigitsUseCase(event.ratio)
                )
            }
            is NutrientGoalEvent.OnFatRatioEnter -> {
                state = state.copy(
                    fatRatio = filterOutDigitsUseCase(event.ratio)
                )
            }
            is NutrientGoalEvent.OnNextClick -> {

                val result = validateNutrientsUseCase(
                    carbRatioText = state.carbRatio,
                    proteinRatioText = state.proteinRatio,
                    fatRatioText = state.fatRatio
                )

                when(result) {
                    is ValidateNutrientsUseCase.Result.Error -> {
                        viewModelScope.launch {
                            _uiEvent.send(UIEvent.ShowSnackbar(result.message))
                        }
                    }
                    is ValidateNutrientsUseCase.Result.Success -> {
                        preferences.saveCarbRatio(result.carbsRatio)
                        preferences.saveProteinRatio(result.proteinRatio)
                        preferences.saveFatRatio(result.fatRatio)

                        viewModelScope.launch {
                            _uiEvent.send(UIEvent.Navigate(Route.TRACKER_OVERVIEW))
                        }
                    }
                }
            }
        }
    }
}