package com.nticoding.calorytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.nticoding.calorytracker.ui.theme.CalorieTrackerTheme
import com.nticoding.onboarding_presentation.welcome.WelcomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalorieTrackerTheme {
                WelcomeScreen()
            }
        }
    }
}