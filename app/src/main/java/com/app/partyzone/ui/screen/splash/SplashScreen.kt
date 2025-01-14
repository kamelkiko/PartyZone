package com.app.partyzone.ui.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.partyzone.R
import com.app.partyzone.design_system.theme.Theme
import com.app.partyzone.ui.navigation.Screen
import com.app.partyzone.ui.util.LocalNavigationProvider
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(viewModel: SplashViewModel = hiltViewModel()) {
    val navController = LocalNavigationProvider.current

    LaunchedEffect(Unit) {
        val isFirstTimeOpenApp = viewModel.isFirstTimeOpenApp()
        val isLoggedIn = viewModel.isUserLoggedIn()
        delay(500)
        if (isFirstTimeOpenApp) {
            navController.popBackStack()
            navController.navigate(Screen.Onboarding)
        } else if (isLoggedIn) {
            navController.popBackStack()
            navController.navigate(Screen.Home)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = com.app.partyzone.design_system.R.drawable.logo),
            contentDescription = stringResource(R.string.logo),
            modifier = Modifier
                .size(256.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = androidx.compose.ui.layout.ContentScale.FillBounds
        )
    }
}