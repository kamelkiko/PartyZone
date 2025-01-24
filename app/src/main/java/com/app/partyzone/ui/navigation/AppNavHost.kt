package com.app.partyzone.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.partyzone.design_system.theme.Theme
import com.app.partyzone.ui.screen.auth.login.LoginScreen
import com.app.partyzone.ui.screen.auth.signup.SignupScreen
import com.app.partyzone.ui.screen.favourite.FavouriteScreen
import com.app.partyzone.ui.screen.home.HomeScreen
import com.app.partyzone.ui.screen.notification.NotificationScreen
import com.app.partyzone.ui.screen.onboarding.OnboardingScreen
import com.app.partyzone.ui.screen.party.PartyScreen
import com.app.partyzone.ui.screen.profile.ProfileScreen
import com.app.partyzone.ui.screen.profile.update.UpdateProfileScreen
import com.app.partyzone.ui.screen.search.SearchScreen
import com.app.partyzone.ui.screen.setting.SettingScreen
import com.app.partyzone.ui.screen.splash.SplashScreen
import com.app.partyzone.ui.util.LocalNavigationProvider

@Composable
fun AppNavHost(innerPadding: PaddingValues) {
    val navController = LocalNavigationProvider.current
    NavHost(
        navController = navController,
        startDestination = Screen.Splash,
        modifier = Modifier
            .background(Theme.colors.primary)
            .padding(paddingValues = innerPadding)
    ) {
        composable<Screen.Splash> {
            SplashScreen()
        }

        composable<Screen.Login> {
            LoginScreen()
        }

        composable<Screen.Signup> {
            SignupScreen()
        }

        composable<Screen.Onboarding> {
            OnboardingScreen()
        }

        composable<Screen.Home> {
            HomeScreen()
        }

        composable<Screen.Favourite> {
            FavouriteScreen()
        }

        composable<Screen.Party> {
            PartyScreen()
        }

        composable<Screen.Setting> {
            SettingScreen()
        }

        composable<Screen.Profile> {
            ProfileScreen()
        }

        composable<Screen.UpdateProfile> {
            UpdateProfileScreen()
        }

        composable<Screen.Search> {
            SearchScreen()
        }

        composable<Screen.Notification> {
            NotificationScreen()
        }
    }
}