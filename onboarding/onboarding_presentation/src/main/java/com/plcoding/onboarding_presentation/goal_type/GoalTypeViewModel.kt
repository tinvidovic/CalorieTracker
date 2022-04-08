package com.plcoding.onboarding_presentation.goal_type

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.core.domain.models.GoalType
import com.plcoding.core.domain.preferences.IPreferences
import com.plcoding.core.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalTypeViewModel @Inject constructor(
    private val preferences: IPreferences
) : ViewModel() {


    var selectedGoalType by mutableStateOf<GoalType>(GoalType.KeepWeight)
        private set

    private val _uiEventChannel = Channel<UiEvent>()
    val uiEventChannel = _uiEventChannel.receiveAsFlow()

    fun onActivityLevelClick(goalType: GoalType){
        selectedGoalType = goalType
    }

    fun onNextClick(){

        viewModelScope.launch {

            preferences.saveGoalType(selectedGoalType)
            _uiEventChannel.send(UiEvent.Success)
        }
    }
}