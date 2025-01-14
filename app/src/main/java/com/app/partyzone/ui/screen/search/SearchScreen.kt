package com.app.partyzone.ui.screen.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.app.partyzone.R
import com.app.partyzone.design_system.composable.PzTextField
import com.app.partyzone.design_system.theme.Theme
import com.app.partyzone.ui.composable.ErrorView
import com.app.partyzone.ui.screen.search.Composable.PzSearchCard
import com.app.partyzone.ui.util.EventHandler

@Composable
fun SearchScreen(searchViewModel: SearchViewModel = hiltViewModel()) {
    val state by searchViewModel.state.collectAsState()

    EventHandler(effects = searchViewModel.effect) { effect, navController ->
        when (effect) {

            is SearchEffect.NavigateToDetails -> {
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
        SearchContent(
            searchState = state.searchState,
            query = state.query,
            isLoading = state.isLoading,
            error = state.error,
            onSearchItemClick = searchViewModel::onClickSearchItem,
            onClickFavIcon = searchViewModel::onClickFavIcon,
            onQueryChange = searchViewModel::onQueryChange,
            onClickRetry = searchViewModel::onClickRetry
        )
    }
}

@Composable
private fun SearchContent(
    query: String,
    isLoading: Boolean,
    error: String?,
    searchState: List<SearchItemState>,
    onSearchItemClick: (String, String) -> Unit,
    onClickFavIcon: (String) -> Unit,
    onQueryChange: (String) -> Unit,
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
            PzTextField(
                text = query,
                onValueChange = onQueryChange,
                textFieldModifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                hint = stringResource(R.string.search),
                keyboardType = KeyboardType.Text,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = com.app.partyzone.design_system.R.drawable.search_icon),
                        contentDescription = stringResource(R.string.search_icon),
                        tint = Theme.colors.contentTertiary
                    )
                },
            )
        }

        items(searchState) {
            PzSearchCard(
                name = it.name,
                location = it.location,
                imageUrl = it.imageUrl,
                isFavourite = it.isFavourite,
                onClick = { onSearchItemClick(it.id, it.type) },
                onClickFavIcon = { onClickFavIcon(it.id) }
            )
        }

        item {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedVisibility(searchState.isEmpty() && isLoading.not()) {
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