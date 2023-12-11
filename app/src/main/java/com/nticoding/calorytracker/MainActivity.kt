package com.nticoding.calorytracker

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nticoding.calorytracker.ui.theme.CalorieTrackerTheme
import com.nticoding.core.navigation.Route
import com.nticoding.calorytracker.navigation.navigate
import com.nticoding.onboarding_presentation.welcome.WelcomeScreen
import com.nticoding.onboarding_presentation.welcome.activity.ActivityScreen
import com.nticoding.onboarding_presentation.welcome.age.AgeScreen
import com.nticoding.onboarding_presentation.welcome.gender.GenderScreen
import com.nticoding.onboarding_presentation.welcome.goal.GoalScreen
import com.nticoding.onboarding_presentation.welcome.height.HeightScreen
import com.nticoding.onboarding_presentation.welcome.weight.WeightScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalorieTrackerTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState,
                ) { _ ->

                    NavHost(
                        navController = navController, startDestination = Route.WELCOME
                    ) {
                        composable(Route.WELCOME) {
                            WelcomeScreen(onNavigate = navController::navigate)
                        }
                        composable(Route.AGE) {
                            AgeScreen(
                                scaffoldState = scaffoldState,
                                onNavigate = navController::navigate
                            )
                        }
                        composable(Route.GENDER) {
                            GenderScreen(onNavigate = navController::navigate)
                        }
                        composable(Route.HEIGHT) {
                            HeightScreen(
                                scaffoldState = scaffoldState,
                                onNavigate = navController::navigate
                            )

                        }
                        composable(Route.WEIGHT) {
                            WeightScreen(
                                scaffoldState = scaffoldState,
                                onNavigate = navController::navigate
                            )
                        }
                        composable(Route.NUTRIENT_GOAL) {

                        }
                        composable(Route.ACTIVITY) {
                            ActivityScreen(
                                onNavigate = navController::navigate
                            )
                        }
                        composable(Route.GOAL) {
                            GoalScreen(
                                onNavigate = navController::navigate
                            )
                        }

                        composable(Route.TRACKER_OVERVIEW) {

                        }
                        composable(Route.SEARCH) {

                        }
                    }

                }

            }
        }
    }
}