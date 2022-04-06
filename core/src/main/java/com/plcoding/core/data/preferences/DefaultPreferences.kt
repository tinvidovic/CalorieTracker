package com.plcoding.core.data.preferences

import android.content.SharedPreferences
import com.plcoding.core.domain.models.ActivityLevel
import com.plcoding.core.domain.models.Gender
import com.plcoding.core.domain.models.GoalType
import com.plcoding.core.domain.models.UserInfo
import com.plcoding.core.domain.preferences.IPreferences
import com.plcoding.core.domain.preferences.IPreferences.PreferencesConstants.KEY_ACTIVITY_LEVEL
import com.plcoding.core.domain.preferences.IPreferences.PreferencesConstants.KEY_AGE
import com.plcoding.core.domain.preferences.IPreferences.PreferencesConstants.KEY_CARB_RATIO
import com.plcoding.core.domain.preferences.IPreferences.PreferencesConstants.KEY_FAT_RATIO
import com.plcoding.core.domain.preferences.IPreferences.PreferencesConstants.KEY_GENDER
import com.plcoding.core.domain.preferences.IPreferences.PreferencesConstants.KEY_GOAL_TYPE
import com.plcoding.core.domain.preferences.IPreferences.PreferencesConstants.KEY_HEIGHT
import com.plcoding.core.domain.preferences.IPreferences.PreferencesConstants.KEY_PROTEIN_RATIO
import com.plcoding.core.domain.preferences.IPreferences.PreferencesConstants.KEY_WEIGHT
import java.util.prefs.Preferences

class DefaultPreferences(
    private val sharedPref: SharedPreferences
) : IPreferences {

    override fun saveGender(gender: Gender) {

        sharedPref.edit()
            .putString(KEY_GENDER, gender.name)
            .apply()
    }

    override fun saveAge(age: Int) {

        sharedPref.edit()
            .putInt(KEY_AGE, age)
            .apply()
    }

    override fun saveWeight(weight: Float) {

        sharedPref.edit()
            .putFloat(KEY_WEIGHT, weight)
            .apply()
    }

    override fun saveHeight(height: Int) {

        sharedPref.edit()
            .putInt(KEY_HEIGHT, height)
            .apply()
    }

    override fun saveActivityLevel(activityLevel: ActivityLevel) {

        sharedPref.edit()
            .putString(KEY_ACTIVITY_LEVEL, activityLevel.name)
            .apply()
    }

    override fun saveGoalType(goalType: GoalType) {

        sharedPref.edit()
            .putString(KEY_GOAL_TYPE, goalType.name)
            .apply()
    }

    override fun saveCarbRatio(ratio: Float) {

        sharedPref.edit()
            .putFloat(KEY_CARB_RATIO, ratio)
            .apply()
    }

    override fun saveProteinRatio(ratio: Float) {

        sharedPref.edit()
            .putFloat(KEY_PROTEIN_RATIO, ratio)
            .apply()
    }

    override fun saveFatRatio(ratio: Float) {

        sharedPref.edit()
            .putFloat(KEY_FAT_RATIO, ratio)
            .apply()
    }

    override fun loadUserInfo(): UserInfo {

        val age = sharedPref.getInt(KEY_AGE, -1)
        val height = sharedPref.getInt(KEY_HEIGHT, -1)
        val weight = sharedPref.getFloat(KEY_WEIGHT, -1f)
        val genderString = sharedPref.getString(KEY_GENDER, null)
        val activityLevelString = sharedPref.getString(KEY_ACTIVITY_LEVEL, null)
        val goalTypeString = sharedPref.getString(KEY_GOAL_TYPE, null)
        val carbRatio = sharedPref.getFloat(KEY_CARB_RATIO, -1f)
        val proteinRatio = sharedPref.getFloat(KEY_PROTEIN_RATIO, -1f)
        val fatRatio = sharedPref.getFloat(KEY_FAT_RATIO, -1f)

        return UserInfo(
            Gender.fromString(genderString?:"male"),
            age,
            weight,
            height,
            ActivityLevel.fromString(activityLevelString?:"medium"),
            GoalType.fromString(goalTypeString?:"keep_weight"),
            carbRatio,
            proteinRatio,
            fatRatio
        )
    }
}