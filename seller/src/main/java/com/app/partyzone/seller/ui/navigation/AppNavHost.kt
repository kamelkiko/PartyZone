package com.app.partyzone.seller.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.partyzone.seller.ui.screen.auth.login.LoginScreen
import com.app.partyzone.seller.ui.screen.auth.signup.SignupScreen
import com.app.partyzone.seller.ui.screen.onboarding.OnboardingScreen
import com.app.partyzone.seller.ui.screen.splash.SplashScreen
import com.app.partyzone.seller.ui.util.LocalNavigationProvider

@Composable
fun AppNavHost(innerPadding: PaddingValues) {
    val navController = LocalNavigationProvider.current
    NavHost(
        navController = navController,
        startDestination = Screen.Splash
    ) {
        composable<Screen.Splash> {
            SplashScreen(innerPadding = innerPadding)
        }

        composable<Screen.Login> {
            LoginScreen(innerPadding = innerPadding)
        }

        composable<Screen.Signup> {
            SignupScreen(innerPadding = innerPadding)
        }

        composable<Screen.Onboarding> {
            OnboardingScreen(innerPadding = innerPadding)
        }
    }
}