package com.app.partyzone.seller.ui.screen.onboarding

sealed interface OnboardingEffect {
    data object NavigateToLogin : OnboardingEffect
}