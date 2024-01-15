package com.nticoding.tracker_presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nticoding.core.domain.use_case.FilterOutDigitsUseCase
import com.nticoding.core.util.UIEvent
import com.nticoding.core.util.UIText
import com.nticoding.tracker_domain.use_case.TrackerUseCases
import com.nticoding.core.R as coreR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val trackerUseCases: TrackerUseCases,
    private val filterOutDigitsUseCase: FilterOutDigitsUseCase,
) : ViewModel() {

    var state by mutableStateOf(SearchState())
        private set

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: SearchEvent) {

        when (event) {
            is SearchEvent.OnQueryChange -> {
                state = state.copy(query = event.query)
            }

            is SearchEvent.OnAmountForFoodChane -> {
                state = state.copy(trackableFoodUiStates = state.trackableFoodUiStates.map {
                    if (it.food == event.food) {
                        it.copy(
                            amount = filterOutDigitsUseCase(event.amount)
                        )
                    } else it
                })
            }

            is SearchEvent.OnSearch -> {
                executeSearch()
            }

            is SearchEvent.OnSearchFocusChange -> {
                state = state.copy(
                    isHintVisible = event.isFocused.not() && state.query.isBlank()
                )
            }

            is SearchEvent.OnToggleTrackableFood -> {
                state = state.copy(trackableFoodUiStates = state.trackableFoodUiStates.map {
                    if (it.food == event.food) {
                        it.copy(isExpanded = it.isExpanded.not())
                    } else it
                })
            }

            is SearchEvent.OnTrackFoodClick -> {
                trackFood(event)
            }
        }
    }

    private fun executeSearch() {
        viewModelScope.launch {
            // Reset list and mark as searching
            state = state.copy(
                isSearching = true,
                trackableFoodUiStates = emptyList()
            )

            trackerUseCases.searchFoodUseCase(state.query)
                .onSuccess { foods ->
                    state = state.copy(
                        trackableFoodUiStates = foods.map {
                            TrackableFoodUiState(it)
                        },
                        isSearching = false,
                        query = ""
                    )
                }
                .onFailure {
                    state = state.copy(
                        isSearching = false
                    )
                    _uiEvent.send(
                        UIEvent.ShowSnackbar(
                            UIText.StringResource(coreR.string.error_something_went_wrong)
                        )
                    )
                }
        }
    }

    private fun trackFood(event: SearchEvent.OnTrackFoodClick) {
        viewModelScope.launch {
            val uiState = state.trackableFoodUiStates.find {
                it.food == event.food
            }
            trackerUseCases.trackFoodUseCase(
                food = uiState?.food ?: return@launch,
                amount = uiState.amount.toIntOrNull() ?: return@launch,
                mealType = event.mealType,
                date = event.date
            )
            _uiEvent.send(UIEvent.NavigateUp)
        }
    }
}