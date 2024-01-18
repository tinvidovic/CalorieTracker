package com.nticoding.calorytracker

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nticoding.core.R
import com.google.common.truth.Truth.assertThat
import com.nticoding.calorytracker.navigation.Route
import com.nticoding.calorytracker.repository.TrackerRepositoryFake
import com.nticoding.calorytracker.ui.theme.CalorieTrackerTheme
import com.nticoding.core.domain.model.ActivityLevel
import com.nticoding.core.domain.model.Gender
import com.nticoding.core.domain.model.GoalType
import com.nticoding.core.domain.model.UserInfo
import com.nticoding.core.domain.preferences.Preferences
import com.nticoding.core.domain.use_case.FilterOutDigitsUseCase
import com.nticoding.onboarding_presentation.activity.ActivityScreen
import com.nticoding.onboarding_presentation.age.AgeScreen
import com.nticoding.onboarding_presentation.gender.GenderScreen
import com.nticoding.onboarding_presentation.goal.GoalScreen
import com.nticoding.onboarding_presentation.height.HeightScreen
import com.nticoding.onboarding_presentation.nutrient_goal.NutrientGoalScreen
import com.nticoding.onboarding_presentation.weight.WeightScreen
import com.nticoding.onboarding_presentation.welcome.WelcomeScreen
import com.nticoding.tracker_domain.model.TrackableFood
import com.nticoding.tracker_domain.use_case.CalculateMealNutrientsUseCase
import com.nticoding.tracker_domain.use_case.DeleteTrackedFoodUseCase
import com.nticoding.tracker_domain.use_case.GetFoodsForDateUseCase
import com.nticoding.tracker_domain.use_case.SearchFoodUseCase
import com.nticoding.tracker_domain.use_case.TrackFoodUseCase
import com.nticoding.tracker_domain.use_case.TrackerUseCases
import com.nticoding.tracker_presentation.search.SearchScreen
import com.nticoding.tracker_presentation.search.SearchViewModel
import com.nticoding.tracker_presentation.tracker_overview.TrackerOverviewScreen
import com.nticoding.tracker_presentation.tracker_overview.TrackerOverviewViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.roundToInt

@HiltAndroidTest
class TrackerOverviewE2E {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var repositoryFake: TrackerRepositoryFake

    private lateinit var trackerUseCases: TrackerUseCases
    private lateinit var preferences: Preferences
    private lateinit var trackerOverviewViewModel: TrackerOverviewViewModel

    private lateinit var searchViewModel: SearchViewModel

    private lateinit var navHostController: NavHostController

    @OptIn(ExperimentalComposeUiApi::class)
    @Before
    fun setUp() {
        preferences = mockk(relaxed = true)
        every {
            preferences.loadUserInfo()
        } returns UserInfo(
            gender = Gender.Male,
            age = 28,
            weight = 80f,
            height = 183,
            activityLevel = ActivityLevel.Medium,
            goalType = GoalType.KeepWeight,
            carbRatio = 0.4f,
            proteinRatio = 0.3f,
            fatRatio = 0.3f,
        )

        repositoryFake = TrackerRepositoryFake()

        trackerUseCases = TrackerUseCases(
            trackFoodUseCase = TrackFoodUseCase(repositoryFake),
            searchFoodUseCase = SearchFoodUseCase(repositoryFake),
            getFoodsForDateUseCase = GetFoodsForDateUseCase(repositoryFake),
            deleteTrackedFoodUseCase = DeleteTrackedFoodUseCase(repositoryFake),
            calculateMealNutrientsUseCase = CalculateMealNutrientsUseCase(preferences),
        )

        trackerOverviewViewModel = TrackerOverviewViewModel(
            preferences = preferences,
            trackerUseCases = trackerUseCases,
        )

        searchViewModel = SearchViewModel(
            trackerUseCases = trackerUseCases,
            filterOutDigitsUseCase = FilterOutDigitsUseCase()
        )

        composeRule.activity.setContent {
            CalorieTrackerTheme {
                val scaffoldState = rememberScaffoldState()
                navHostController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState,
                ) { _ ->

                    NavHost(
                        navController = navHostController,
                        startDestination = Route.TRACKER_OVERVIEW
                    ) {
                        composable(Route.TRACKER_OVERVIEW) {
                            TrackerOverviewScreen(
                                onNavigateToSearch = { mealName, day, month, year ->
                                    navHostController.navigate(
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
                            )
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
                                    navHostController.navigateUp()
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
    fun addBreakfast_appearsUnderBreakfast_nutrientsCalculatedProperly() {

        repositoryFake.searchResults = listOf(
            TrackableFood(
                name = "Banana 1",
                imageUrl = null,
                caloriesPer100g = 150,
                proteinPer100g = 5,
                carbsPer100g = 50,
                fatPer100g = 1
            ),
        )

        val userAddedAmount = 150

        val expectedCalories = (1.5f * 150).roundToInt()
        val expectedCarbs = (1.5f * 50).roundToInt()
        val expectedProtein = (1.5f * 5).roundToInt()
        val expectedFat = (1.5f * 1).roundToInt()

        // Check if breakfast button is not visible until toggled
        composeRule
            .onNodeWithText("Add Breakfast")
            .assertDoesNotExist()

        // Click on the breakfast section
        composeRule
            .onNodeWithContentDescription("Breakfast")
            .performClick()

        // Check if breakfast button is nnoow visible
        composeRule
            .onNodeWithText("Add Breakfast")
            .assertIsDisplayed()

        // Click on the Add Breakfast button
        composeRule
            .onNodeWithText("Add Breakfast")
            .performClick()

        // Check if we are navigated to SearchScreen
        assertThat(
            navHostController
                .currentDestination
                ?.route
                ?.startsWith(Route.SEARCH)
        ).isTrue()

        // Find text field and search for banana
        composeRule
            .onNodeWithTag("search_text_field")
            .performTextInput("banana")

        // Perform search by clicking on the icon
        composeRule
            .onNodeWithContentDescription("Search...")
            .performClick()

        // Expand
        composeRule
            .onNodeWithText("Carbs")
            .performClick()

        // Insert the amount
        composeRule
            .onNodeWithContentDescription("Amount")
            .performTextInput(userAddedAmount.toString())

        // Track the food
        composeRule
            .onNodeWithContentDescription("Track")
            .performClick()

        // Check if we are navigated to TrackerOverviewScreen
        assertThat(
            navHostController
                .currentDestination
                ?.route
                ?.startsWith(Route.TRACKER_OVERVIEW)
        ).isTrue()

        // Check if all of the expected values are displayed somewhere
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