package com.plcoding.onboarding_presentation.activity_level

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.core.domain.models.ActivityLevel
import com.plcoding.core.domain.preferences.IPreferences
import com.plcoding.core.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityLevelViewModel @Inject constructor(
    private val preferences: IPreferences
) : ViewModel() {


    var selectedActivityLevel by mutableStateOf<ActivityLevel>(ActivityLevel.Medium)
        private set

    private val _uiEventChannel = Channel<UiEvent>()
    val uiEventChannel = _uiEventChannel.receiveAsFlow()

    fun onActivityLevelClick(activityLevel: ActivityLevel){
        selectedActivityLevel = activityLevel
    }

    fun onNextClick(){

        viewModelScope.launch {

            preferences.saveActivityLevel(selectedActivityLevel)
            _uiEventChannel.send(UiEvent.Success)
        }
    }
}