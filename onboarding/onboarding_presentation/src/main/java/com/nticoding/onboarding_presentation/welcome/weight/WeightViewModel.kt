package com.nticoding.onboarding_presentation.welcome.weight

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nticoding.core.R
import com.nticoding.core.domain.preferences.Preferences
import com.nticoding.core.navigation.Route
import com.nticoding.core.util.UIEvent
import com.nticoding.core.util.UIText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeightViewModel @Inject constructor(
    private val preferences: Preferences,
): ViewModel() {

    var weight by mutableStateOf("80.0")
        private set

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onWeightEnter(weight: String) {

        if (weight.length <= 5) {
            this.weight = weight
        }
    }

    fun onNextClick() {

        viewModelScope.launch {

            val weightNumber = weight.toFloatOrNull() ?: kotlin.run {

                _uiEvent.send(
                    UIEvent.ShowSnackbar(
                        UIText.StringResource(R.string.error_weight_cant_be_empty)
                    )
                )
                return@launch
            }

            preferences.saveWeight(weightNumber)
            _uiEvent.send(UIEvent.Navigate(Route.ACTIVITY))
        }
    }
}