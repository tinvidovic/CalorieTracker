package com.plcoding.onboarding_presentation.age

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
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
class AgeViewModel @Inject constructor(
    private val preferences: IPreferences,
    private val filterOutDigitsUseCase: FilterOutDigitsUseCase
) : ViewModel() {

    var age by mutableStateOf("20")
        private set

    private val _uiEventChannel = Channel<UiEvent>()
    val uiEventChannel = _uiEventChannel.receiveAsFlow()

    fun onAgeEnter(age: String) {

        if (age.length <= 3) {
            this.age = filterOutDigitsUseCase(age)
        }
    }

    fun onNextClick() {

        viewModelScope.launch {
            val ageNumber = age.toIntOrNull() ?: kotlin.run {

                _uiEventChannel.send(
                    UiEvent.ShowSnackbar(UiText.StringResource(R.string.error_age_cant_be_empty))
                )
                return@launch
            }

            preferences.saveAge(ageNumber)
            _uiEventChannel.send(UiEvent.Navigate(Route.HEIGHT))
        }
    }
}