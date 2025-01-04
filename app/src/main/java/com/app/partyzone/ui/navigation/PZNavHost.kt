package com.app.partyzone.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.partyzone.ui.screen.auth.login.LoginScreen
import com.app.partyzone.ui.screen.auth.signup.SignupScreen
import com.app.partyzone.ui.screen.home.HomeScreen
import com.app.partyzone.ui.util.LocalNavigationProvider

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PZNavHost(innerPadding: PaddingValues, onGoogleClick: () -> Unit) {
    val navController = LocalNavigationProvider.current
    NavHost(
        navController = navController,
        startDestination = Signup
    ) {
        composable<Login> {
            LoginScreen(innerPadding = innerPadding, onGoogleClick)
        }
        composable<Signup> {
            SignupScreen(innerPadding = innerPadding, onGoogleClick)
        }
        composable<Home> {
            HomeScreen()
        }
    }
}