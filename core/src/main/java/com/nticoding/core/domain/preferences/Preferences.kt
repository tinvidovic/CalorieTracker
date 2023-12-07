package com.nticoding.core.domain.preferences

import com.nticoding.core.domain.model.ActivityLevel
import com.nticoding.core.domain.model.Gender
import com.nticoding.core.domain.model.GoalType
import com.nticoding.core.domain.model.UserInfo

interface Preferences {
    fun saveGender(gender: Gender)
    fun saveAge(age: Int)
    fun saveWeight(weight: Float)
    fun saveHeight(height: Int)
    fun saveActivityLevel(level: ActivityLevel)
    fun saveGoalType(type: GoalType)
    fun saveCarbRatio(ratio: Float)
    fun saveProteinRatio(ratio: Float)
    fun saveFatRatio(ratio: Float)
    fun loadUserInfo(): UserInfo
}