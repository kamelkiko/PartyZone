package com.app.partyzone.ui.screen.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.partyzone.R
import com.app.partyzone.design_system.composable.PzButton
import com.app.partyzone.design_system.theme.Theme
import com.app.partyzone.ui.navigation.Screen
import com.app.partyzone.ui.screen.onboarding.composable.CenteredContentWithImageAndText
import com.app.partyzone.ui.screen.onboarding.composable.GradientPagerIndicator
import com.app.partyzone.ui.util.EventHandler
import com.app.partyzone.ui.util.LocalNavigationProvider
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(onboardingViewModel: OnboardingViewModel = hiltViewModel()) {
    EventHandler(effects = onboardingViewModel.effect) { effect, nav ->
        when (effect) {
            is OnboardingEffect.NavigateToLogin -> {
                nav.navigate(Screen.Login) {
                    popUpTo(Screen.Onboarding) { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary)
    ) {
        OnboardingContent(
            onClickGetStart = onboardingViewModel::setFirstTimeOpenApp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OnboardingContent(
    onClickGetStart: () -> Unit,
) {
    val pagerState = rememberPagerState { 3 }
    val coroutineScope = rememberCoroutineScope()
    val nav = LocalNavigationProvider.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // HorizontalPager for swiping between screens
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
        ) { page ->
            when (page) {
                0 -> OnboardingScreen1()
                1 -> OnboardingScreen2()
                2 -> OnboardingScreen3()
            }
        }

        GradientPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.height(16.dp))

        PzButton(
            title = if (pagerState.currentPage == 2) stringResource(R.string.get_started)
            else
                stringResource(R.string.next),
            onClick = {
                if (pagerState.currentPage < 2) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    onClickGetStart()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(56.dp)
        )
    }
}


@Composable
fun OnboardingScreen1() {
    CenteredContentWithImageAndText(
        imageRes = com.app.partyzone.design_system.R.drawable.logo,
        title = stringResource(R.string.find_your_favourite_parties_here),
        subtitle = stringResource(R.string.welcome_to_our_app_get_started_with_the_best_experience),
        titleColor = Theme.colors.contentPrimary,
        subtitleColor = Theme.colors.contentSecondary,
        titleStyle = Theme.typography.headlineLarge,
        subtitleStyle = Theme.typography.title
    )
}

@Composable
fun OnboardingScreen2() {
    CenteredContentWithImageAndText(
        imageRes = com.app.partyzone.design_system.R.drawable.logo,
        title = stringResource(R.string.explore_your_nearby_party_place_here),
        subtitle = stringResource(R.string.explore_amazing_features_and_services_tailored_for_you),
        titleColor = Theme.colors.contentPrimary,
        subtitleColor = Theme.colors.contentSecondary,
        titleStyle = Theme.typography.headlineLarge,
        subtitleStyle = Theme.typography.title
    )
}

@Composable
fun OnboardingScreen3() {
    CenteredContentWithImageAndText(
        imageRes = com.app.partyzone.design_system.R.drawable.logo,
        title = stringResource(R.string.get_started),
        subtitle = stringResource(R.string.start_your_journey_with_us_and_enjoy_seamless_experience),
        titleColor = Theme.colors.contentPrimary,
        subtitleColor = Theme.colors.contentSecondary,
        titleStyle = Theme.typography.headlineLarge,
        subtitleStyle = Theme.typography.title
    )
}