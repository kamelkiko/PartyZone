package com.app.partyzone.ui.screen.party

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.app.partyzone.R
import com.app.partyzone.design_system.composable.PzAnimatedTabLayout
import com.app.partyzone.design_system.theme.Theme
import com.app.partyzone.ui.util.EventHandler
import kotlin.reflect.KFunction2

@Composable
fun PartyScreen(partyViewModel: PartyViewModel = hiltViewModel()) {
    val state by partyViewModel.state.collectAsState()

    EventHandler(effects = partyViewModel.effect) { effect, navController ->
        when (effect) {

            is PartyEffect.NavigateToDetails -> {
                // navController.navigate(Screen.Notification)
            }

            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary)
    ) {
        PartyContent(
            partyState = state.partyState,
            onClickItem = partyViewModel::onClickFavouriteItem
        )
    }
}

@Composable
private fun PartyContent(
    partyState: List<PartyItemState>,
    onClickItem: (String, String) -> Unit,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_anim))
    val progress by animateLottieCompositionAsState(composition, iterations = Int.MAX_VALUE)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.alltickets),
                color = Theme.colors.contentPrimary,
                style = Theme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
        }

        item {
            PzAnimatedTabLayout(
                tabItems = Status.entries,
                selectedTab = Status.Upcoming,
                onTabSelected = { },
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalPadding = 0.dp,
            ) { type ->
                Text(
                    text = type.name,
                    style = Theme.typography.titleMedium,
                    color = animateColorAsState(
                        if (type == Status.Upcoming) Theme.colors.primary
                        else Theme.colors.contentPrimary, label = ""
                    ).value,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

val tabItems = listOf("Upcoming", "Completed", "Cancelled")

enum class Status {
    Upcoming,
    Completed,
    Cancelled
}