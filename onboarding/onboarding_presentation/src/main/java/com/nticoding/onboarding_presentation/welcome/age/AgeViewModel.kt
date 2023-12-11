package com.nticoding.onboarding_presentation.welcome.age

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
import com.nticoding.core.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AgeViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterOutDigitsUseCase: FilterOutDigitsUseCase,
): ViewModel() {

    var age by mutableStateOf("20")
        private set

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAgeEnter(age: String) {

        if (age.length <= 3) {
            this.age = filterOutDigitsUseCase(age)
        }
    }

    fun onNextClick() {

        viewModelScope.launch {

            val ageNumber = age.toIntOrNull() ?: kotlin.run {

                _uiEvent.send(
                    UIEvent.ShowSnackbar(
                        UIText.StringResource(R.string.error_age_cant_be_empty)
                    )
                )
                return@launch
            }

            preferences.saveAge(ageNumber)
            _uiEvent.send(UIEvent.Navigate(Route.HEIGHT))
        }
    }
}