package com.app.partyzone.ui.screen.party

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.app.partyzone.R
import com.app.partyzone.core.domain.entity.Status
import com.app.partyzone.design_system.composable.PzAnimatedTabLayout
import com.app.partyzone.design_system.theme.Theme
import com.app.partyzone.ui.composable.ErrorView
import com.app.partyzone.ui.screen.favourite.composable.PzFavouriteCard
import com.app.partyzone.ui.util.EventHandler

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
            selectedType = state.selectedType,
            isLoading = state.isLoading,
            error = state.error,
            onClickItem = partyViewModel::onClickFavouriteItem,
            onClickTab = partyViewModel::onClickTab,
            onClickRetry = partyViewModel::onClickRetry
        )
    }
}

@Composable
private fun PartyContent(
    partyState: List<PartyItemState>,
    selectedType: Status,
    isLoading: Boolean,
    error: String?,
    onClickItem: (String, String) -> Unit,
    onClickTab: (Status) -> Unit,
    onClickRetry: () -> Unit,
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
                selectedTab = selectedType,
                onTabSelected = { onClickTab(it) },
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
        when (selectedType) {
            Status.Upcoming -> {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AnimatedVisibility(partyState.none { it.status == Status.Upcoming.name } && isLoading.not() && error == null) {
                            LottieAnimation(
                                composition = composition,
                                progress = { progress },
                                modifier = Modifier
                                    .size(256.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
                items(partyState.filter { it.status == Status.Upcoming.name }) {
                    PzFavouriteCard(
                        name = it.itemName + " (${it.type})",
                        location = it.status,
                        imageUrl = it.itemImageUrl,
                        onClick = { onClickItem(it.id, it.type) },
                        price = null
                    )
                }
            }

            Status.Completed -> {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AnimatedVisibility(partyState.none { it.status == Status.Completed.name } && isLoading.not() && error == null) {
                            LottieAnimation(
                                composition = composition,
                                progress = { progress },
                                modifier = Modifier
                                    .size(256.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
                items(partyState.filter { it.status == Status.Completed.name }) {
                    PzFavouriteCard(
                        name = it.itemName + " (${it.type})",
                        location = it.status,
                        imageUrl = it.itemImageUrl,
                        onClick = { onClickItem(it.id, it.type) },
                        price = null
                    )
                }
            }

            Status.Cancelled -> {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AnimatedVisibility(partyState.none { it.status == Status.Cancelled.name } && isLoading.not() && error == null) {
                            LottieAnimation(
                                composition = composition,
                                progress = { progress },
                                modifier = Modifier
                                    .size(256.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
                items(partyState.filter { it.status == Status.Cancelled.name }) {
                    PzFavouriteCard(
                        name = it.itemName + " (${it.type})",
                        location = it.status,
                        imageUrl = it.itemImageUrl,
                        onClick = { onClickItem(it.id, it.type) },
                        price = null
                    )
                }
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedVisibility(partyState.isEmpty() && isLoading.not() && error == null) {
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier
                            .size(256.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(visible = isLoading.not() && error != null) {
                    ErrorView(
                        error = error,
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentHeight()
                            .align(Alignment.CenterHorizontally),
                        onClickRetry = onClickRetry
                    )
                }
            }
        }

        item {
            Column {
                AnimatedVisibility(visible = isLoading) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .align(Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            color = Theme.colors.contentPrimary,
                        )
                    }
                }
            }
        }
    }
}