package com.app.partyzone.ui.screen.favourite

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import com.app.partyzone.R
import com.app.partyzone.design_system.theme.Theme
import com.app.partyzone.ui.composable.ErrorView
import com.app.partyzone.ui.composable.PzCard
import com.app.partyzone.ui.util.EventHandler

@Composable
fun FavouriteScreen(favouriteViewModel: FavouriteViewModel = hiltViewModel()) {
    val state by favouriteViewModel.state.collectAsState()

    EventHandler(effects = favouriteViewModel.effect) { effect, navController ->
        when (effect) {

            is FavouriteEffect.NavigateToDetails -> {
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
        AnimatedVisibility(visible = state.isLoading) {
            Column(
                Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
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
        AnimatedVisibility(visible = state.isLoading.not() && state.error != null) {
            ErrorView(
                error = state.error,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight()
                    .align(Alignment.Center),
                onClickRetry = favouriteViewModel::onClickRetry
            )
        }
        AnimatedVisibility(visible = state.isLoading.not() && state.error.isNullOrEmpty()) {
            FavouriteContent(
                favouriteState = state.favouriteState,
                onFavouriteItemClick = favouriteViewModel::onClickFavouriteItem
            )
        }
    }
}

@Composable
private fun FavouriteContent(
    favouriteState: List<FavouriteItemState>,
    onFavouriteItemClick: (String, String) -> Unit,
) {
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
                text = stringResource(id = R.string.favourite),
                color = Theme.colors.contentPrimary,
                style = Theme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
        }

        items(favouriteState) {
            PzCard(
                name = it.name,
                location = it.location,
                imageUrl = it.imageUrl,
                price = it.price,
                onClick = { onFavouriteItemClick(it.id, it.type) }
            )
        }
    }
}