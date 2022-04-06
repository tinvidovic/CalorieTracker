package com.plcoding.onboarding_domain.di

import com.plcoding.onboarding_domain.use_cases.ValidateNutrientsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
class OnboardingDomainModule {

    @Provides
    @ViewModelScoped
    fun provideValidateNutrientsUseCase() : ValidateNutrientsUseCase {
        return ValidateNutrientsUseCase()
    }
}