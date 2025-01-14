package com.app.partyzone.ui.screen.onboarding

sealed interface OnboardingEffect {
    data object NavigateToLogin : OnboardingEffect
}