package com.nticoding.onboarding_presentation.welcome.goal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nticoding.core.domain.model.GoalType
import com.nticoding.core.domain.preferences.Preferences
import com.nticoding.core.navigation.Route
import com.nticoding.core.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    private val preferences: Preferences
): ViewModel() {

    var selectedGoalType by mutableStateOf<GoalType>(GoalType.KeepWeight)
        private set

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onGoalTypeClick(goalType: GoalType) {
        selectedGoalType = goalType
    }

    fun onNextClick() {
        viewModelScope.launch {
            preferences.saveGoalType(selectedGoalType)
            _uiEvent.send(UIEvent.Navigate(Route.NUTRIENT_GOAL))
        }
    }
}