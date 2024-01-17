package com.nticoding.onboarding_presentation.height

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nticoding.core.domain.preferences.Preferences
import com.nticoding.core.domain.use_case.FilterOutDigitsUseCase
import com.nticoding.core.util.UIEvent
import com.nticoding.core.util.UIText
import com.nticoding.core.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HeightViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterOutDigitsUseCase: FilterOutDigitsUseCase,
): ViewModel() {

    var height by mutableStateOf("180")
        private set

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onHeightEnter(height: String) {

        if (height.length <= 3) {
            this.height = filterOutDigitsUseCase(height)
        }
    }

    fun onNextClick() {

        viewModelScope.launch {

            val heightNumber = height.toIntOrNull() ?: kotlin.run {

                _uiEvent.send(
                    UIEvent.ShowSnackbar(
                        UIText.StringResource(R.string.error_height_cant_be_empty)
                    )
                )
                return@launch
            }

            preferences.saveHeight(heightNumber)
            _uiEvent.send(UIEvent.Success)
        }
    }
}