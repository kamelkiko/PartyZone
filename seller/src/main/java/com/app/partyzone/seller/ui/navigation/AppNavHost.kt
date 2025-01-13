package com.app.partyzone.seller.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.partyzone.seller.ui.screen.auth.login.LoginScreen
import com.app.partyzone.seller.ui.screen.auth.signup.SignupScreen
import com.app.partyzone.seller.ui.screen.favourite.FavouriteScreen
import com.app.partyzone.seller.ui.screen.home.HomeScreen
import com.app.partyzone.seller.ui.screen.notification.NotificationScreen
import com.app.partyzone.seller.ui.screen.onboarding.OnboardingScreen
import com.app.partyzone.seller.ui.screen.party.PartyScreen
import com.app.partyzone.seller.ui.screen.profile.ProfileScreen
import com.app.partyzone.seller.ui.screen.search.SearchScreen
import com.app.partyzone.seller.ui.screen.setting.SettingScreen
import com.app.partyzone.seller.ui.screen.splash.SplashScreen
import com.app.partyzone.seller.ui.util.LocalNavigationProvider

@Composable
fun AppNavHost(innerPadding: PaddingValues) {
    val navController = LocalNavigationProvider.current
    NavHost(
        navController = navController,
        startDestination = Screen.Home
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

        composable<Screen.Home> {
            HomeScreen(innerPadding = innerPadding)
        }

        composable<Screen.Favourite> {
            FavouriteScreen(innerPadding = innerPadding)
        }

        composable<Screen.Party> {
            PartyScreen(innerPadding = innerPadding)
        }

        composable<Screen.Setting> {
            SettingScreen(innerPadding = innerPadding)
        }

        composable<Screen.Profile> {
            ProfileScreen(innerPadding = innerPadding)
        }

        composable<Screen.Search> {
            SearchScreen(innerPadding = innerPadding)
        }

        composable<Screen.Notification> {
            NotificationScreen(innerPadding = innerPadding)
        }
    }
}