package com.app.partyzone.ui.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.app.partyzone.R
import com.app.partyzone.design_system.composable.PzChip
import com.app.partyzone.design_system.composable.PzIconButton
import com.app.partyzone.design_system.theme.Theme
import com.app.partyzone.design_system.theme.brush
import com.app.partyzone.ui.composable.ErrorView
import com.app.partyzone.ui.navigation.Screen
import com.app.partyzone.ui.util.ComposableLifecycle
import com.app.partyzone.ui.util.EventHandler

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = hiltViewModel()) {
    val state by homeViewModel.state.collectAsState()

    ComposableLifecycle { _, event ->
        if (event == Lifecycle.Event.ON_RESUME)
            homeViewModel.hasNotifications()
    }

    EventHandler(effects = homeViewModel.effect) { effect, navController ->
        when (effect) {

            is HomeEffect.NavigateToNotification -> {
                navController.navigate(Screen.Notification)
            }

            is HomeEffect.NavigateToSearch -> {
                navController.navigate(Screen.Search)
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
                onClickRetry = homeViewModel::onClickRetry
            )
        }
        AnimatedVisibility(visible = state.isLoading.not() && state.error.isNullOrEmpty()) {
            HomeContent(
                name = state.userState.name,
                hasNotifications = state.hasNotifications,
                onClickNotification = homeViewModel::onClickNotification,
                onClickSearch = homeViewModel::onClickSearch
            )
        }
    }
}

@Composable
private fun HomeContent(
    name: String,
    hasNotifications: Boolean,
    onClickNotification: () -> Unit,
    onClickSearch: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.welcome),
                    color = Theme.colors.contentPrimary,
                    style = Theme.typography.titleLarge
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = com.app.partyzone.design_system.R.drawable.user_icon),
                        contentDescription = stringResource(R.string.map_icon),
                        modifier = Modifier
                            .size(24.dp)
                            .graphicsLayer(alpha = 0.99f)
                            .drawWithCache {
                                onDrawWithContent {
                                    drawContent()
                                    drawRect(brush, blendMode = BlendMode.SrcAtop)
                                }
                            }
                    )
                    Text(
                        text = name,
                        color = Theme.colors.contentSecondary,
                        style = Theme.typography.title
                    )
                }
            }
            PzIconButton(
                painter = painterResource(id = com.app.partyzone.design_system.R.drawable.notification_icon),
                onClick = onClickNotification,
                tint = if (hasNotifications) Color(0xFFFB0160) else Theme.colors.contentPrimary
            ) {
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp)
                .border(1.dp, Theme.colors.contentBorder, RoundedCornerShape(Theme.radius.large))
                .clip(RoundedCornerShape(Theme.radius.large))
                .background(Theme.colors.primary, RoundedCornerShape(Theme.radius.large))
                .clickable { onClickSearch() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    painter = painterResource(id = com.app.partyzone.design_system.R.drawable.search_icon),
                    contentDescription = stringResource(R.string.search_icon),
                    tint = Theme.colors.contentTertiary,
                    modifier = Modifier
                        .size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.search),
                    color = Theme.colors.contentTertiary,
                    style = Theme.typography.title
                )
            }
            Icon(
                painter = painterResource(id = com.app.partyzone.design_system.R.drawable.filter_icon),
                contentDescription = stringResource(R.string.search_icon),
                tint = Theme.colors.contentPrimary,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(24.dp)
                    .clickable { }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        PzChip(label = "Music", isSelected = false, onClick = {})
    }
}