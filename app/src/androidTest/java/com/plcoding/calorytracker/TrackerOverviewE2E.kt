package com.plcoding.calorytracker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.google.common.truth.Truth.assertThat
import com.plcoding.calorytracker.navigation.Route
import com.plcoding.calorytracker.repository.TrackerRepositoryFake
import com.plcoding.calorytracker.ui.theme.CaloryTrackerTheme
import com.plcoding.core.domain.models.ActivityLevel
import com.plcoding.core.domain.models.Gender
import com.plcoding.core.domain.models.GoalType
import com.plcoding.core.domain.models.UserInfo
import com.plcoding.core.domain.preferences.IPreferences
import com.plcoding.core.domain.use_cases.FilterOutDigitsUseCase
import com.plcoding.tracker_domain.models.TrackableFood
import com.plcoding.tracker_domain.use_cases.*
import com.plcoding.tracker_presentation.search.SearchScreen
import com.plcoding.tracker_presentation.search.SearchViewModel
import com.plcoding.tracker_presentation.tracker_overview.TrackerOverviewScreen
import com.plcoding.tracker_presentation.tracker_overview.TrackerOverviewViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.roundToInt

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalCoilApi
@HiltAndroidTest
class TrackerOverviewE2E {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)


    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var repositoryFake: TrackerRepositoryFake
    private lateinit var trackerUseCases: TrackerUseCases
    private lateinit var preferences: IPreferences
    private lateinit var trackerOverviewViewModel: TrackerOverviewViewModel
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var navController: NavHostController

    @Before
    fun setUp() {

        // Initialize dependencies
        preferences = mockk(relaxed = true)
        every {
            preferences.loadUserInfo()
        } returns UserInfo(
            gender = Gender.Male,
            age = 20,
            weight = 80f,
            height = 180,
            activityLevel = ActivityLevel.Medium,
            goalType = GoalType.KeepWeight,
            carbRatio = 0.4f,
            proteinRatio = 0.3f,
            fatRatio = 0.3f
        )

        repositoryFake = TrackerRepositoryFake()

        trackerUseCases = TrackerUseCases(
            trackFoodUseCase = TrackFoodUseCase(repositoryFake),
            searchFoodUseCase = SearchFoodUseCase(repositoryFake),
            getFoodsForDateUseCase = GetFoodsForDateUseCase(repositoryFake),
            deleteTrackedFoodUseCase = DeleteTrackedFoodUseCase(repositoryFake),
            calculateMealNutrientsUseCase = CalculateMealNutrientsUseCase(preferences)
        )

        trackerOverviewViewModel = TrackerOverviewViewModel(
            preferences = preferences,
            trackerUseCases = trackerUseCases
        )

        searchViewModel = SearchViewModel(
            trackerUserCases = trackerUseCases,
            filterOutDigitsUseCase = FilterOutDigitsUseCase()
        )

        // Set Up Composable
        composeRule.setContent {

            CaloryTrackerTheme {

                val scaffoldState = rememberScaffoldState()

                navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Route.TRACKER_OVERVIEW
                    ) {
                        composable(Route.TRACKER_OVERVIEW) {
                            TrackerOverviewScreen(
                                onNavigateToSearch = { mealName, day, month, year ->
                                    navController.navigate(
                                        Route.SEARCH + "/$mealName" +
                                                "/$day" +
                                                "/$month" +
                                                "/$year"
                                    )

                                },
                                viewModel = trackerOverviewViewModel
                            )
                        }
                        composable(
                            route = Route.SEARCH + "/{mealName}/{dayOfMonth}/{month}/{year}",
                            arguments = listOf(
                                navArgument("mealName") {
                                    type = NavType.StringType
                                },
                                navArgument("dayOfMonth") {
                                    type = NavType.IntType
                                },
                                navArgument("month") {
                                    type = NavType.IntType
                                },
                                navArgument("year") {
                                    type = NavType.IntType
                                },
                            ),
                        ) {
                            val mealName = it.arguments?.getString("mealName")!!
                            val dayOfMonth = it.arguments?.getInt("dayOfMonth")!!
                            val month = it.arguments?.getInt("month")!!
                            val year = it.arguments?.getInt("year")!!
                            SearchScreen(
                                scaffoldState = scaffoldState,
                                mealName = mealName,
                                dayOfMonth = dayOfMonth,
                                month = month,
                                year = year,
                                onNavigateUp = {
                                    navController.navigateUp()
                                },
                                viewModel = searchViewModel
                            )
                        }
                    }
                }
            }
        }
    }

    @Test
    fun add_breakfast_appears_under_breakfast_and_nutrients_properly_calculated() {

        repositoryFake.searchResults = listOf(
            TrackableFood(
                name = "banana",
                imageUrl = null,
                caloriesPer100g = 150,
                carbsPer100g = 50,
                proteinPer100g = 5,
                fatsPer100g = 1
            )
        )

        val addedAmount = 150
        val expectedCalories = (1.5f * 150).roundToInt()
        val expectedCarbs = (1.5f * 50).roundToInt()
        val expectedProtein = (1.5f * 5).roundToInt()
        val expectedFat = (1.5f * 1).roundToInt()

        composeRule
            .onNodeWithText(
                "Add Breakfast"
            ).assertDoesNotExist()

        composeRule
            .onNodeWithContentDescription("Breakfast")
            .performClick()

        // Check if the breakfast button shows up
        composeRule
            .onNodeWithText(
                "Add Breakfast"
            ).assertIsDisplayed()

        composeRule
            .onNodeWithText("Add Breakfast")
            .performClick()

        // Does it navigate to SearchScreen
        assertThat(
            navController
                .currentDestination
                ?.route
                ?.startsWith(Route.SEARCH)
        ).isTrue()

        composeRule
            .onNodeWithTag("search_text_field")
            .performTextInput("banana")

        composeRule
            .onNodeWithContentDescription("Search…")
            .performClick()

        composeRule
            .onNodeWithText("Carbs")
            .performClick()

        composeRule
            .onNodeWithContentDescription(
                "Amount"
            )
            .performTextInput(addedAmount.toString())

        composeRule
            .onNodeWithContentDescription("Track")
            .performClick()

        // Does it navigate back to TrackerOverview
        assertThat(
            navController
                .currentDestination
                ?.route
                ?.startsWith(Route.TRACKER_OVERVIEW)
        ).isTrue()

        composeRule
            .onAllNodesWithText(expectedCarbs.toString())
            .onFirst()
            .assertIsDisplayed()

        composeRule
            .onAllNodesWithText(expectedProtein.toString())
            .onFirst()
            .assertIsDisplayed()

        composeRule
            .onAllNodesWithText(expectedFat.toString())
            .onFirst()
            .assertIsDisplayed()

        composeRule
            .onAllNodesWithText(expectedCalories.toString())
            .onFirst()
            .assertIsDisplayed()

    }
}