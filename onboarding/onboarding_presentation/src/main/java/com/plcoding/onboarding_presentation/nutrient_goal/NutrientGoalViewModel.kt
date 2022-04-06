package com.plcoding.onboarding_presentation.nutrient_goal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.core.domain.preferences.IPreferences
import com.plcoding.core.domain.use_cases.FilterOutDigitsUseCase
import com.plcoding.core.navigation.Route
import com.plcoding.core.util.UiEvent
import com.plcoding.onboarding_domain.use_cases.ValidateNutrientsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutrientGoalViewModel @Inject constructor(
    private val preferences: IPreferences,
    private val filterOutDigitsUseCase: FilterOutDigitsUseCase,
    private val validateNutrientsUseCase: ValidateNutrientsUseCase
    ) : ViewModel() {

    var state by mutableStateOf(NutrientGoalState())
        private set

    private val _uiEventChannel = Channel<UiEvent>()
    val uiEventChannel = _uiEventChannel.receiveAsFlow()

    fun onEvent(event: NutrientGoalEvent) {

        when (event) {
            is NutrientGoalEvent.OnCarbRatioEnter -> {

                state = state.copy(
                    carbRatio = filterOutDigitsUseCase(event.ratio)
                )
            }
            is NutrientGoalEvent.OnProteinRatioEnter -> {

                state = state.copy(
                    proteinRatio = event.ratio
                )
            }
            is NutrientGoalEvent.OnFatRatioEnter -> {

                state = state.copy(
                    fatRatio = event.ratio
                )
            }
            is NutrientGoalEvent.onNextClick -> {

                val result = validateNutrientsUseCase(
                    state.carbRatio,
                    state.proteinRatio,
                    state.fatRatio
                )

                when(result){

                    is ValidateNutrientsUseCase.Result.Success -> {

                        preferences.saveCarbRatio(result.carbRatio)
                        preferences.saveProteinRatio(result.proteinRatio)
                        preferences.saveFatRatio(result.fatRatio)

                        viewModelScope.launch {
                            _uiEventChannel.send(
                                UiEvent.Navigate(Route.TRACKER_OVERVIEW)
                            )
                        }
                    }
                    is ValidateNutrientsUseCase.Result.Error -> {
                        viewModelScope.launch {

                            _uiEventChannel.send(UiEvent.ShowSnackbar(result.message))
                        }
                    }
                }
            }
        }
    }

}