package com.plcoding.onboarding_presentation.weight

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.core.domain.preferences.IPreferences
import com.plcoding.core.domain.use_cases.FilterOutDigitsUseCase
import com.plcoding.core.navigation.Route
import com.plcoding.core.util.UiEvent
import com.plcoding.core.util.UiText
import com.plcoding.onboarding_presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeightViewModel @Inject constructor(
    private val preferences: IPreferences
) : ViewModel() {

    var weight by mutableStateOf("80.0")
        private set

    private val _uiEventChannel = Channel<UiEvent>()
    val uiEventChannel = _uiEventChannel.receiveAsFlow()

    fun onWeightEnter(weight: String) {

        if (weight.length <= 5){
            this.weight = weight
        }
    }

    fun onNextClick() {

        viewModelScope.launch {
            val weightNumber = weight.toFloatOrNull() ?: kotlin.run {

                _uiEventChannel.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_weight_cant_be_empty))
                )
                return@launch
            }

            preferences.saveWeight(weightNumber)
            _uiEventChannel.send(UiEvent.Navigate(Route.ACTIVITY))
        }
    }
}