package com.app.partyzone.ui.screen.notification

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
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

@Composable
fun NotificationScreen(notificationViewModel: NotificationViewModel = hiltViewModel()) {
    val state by notificationViewModel.state.collectAsState()

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
                onClickRetry = notificationViewModel::onClickRetry
            )
        }
        AnimatedVisibility(visible = state.isLoading.not() && state.error.isNullOrEmpty()) {
            NotificationContent()
        }
    }
}

@Composable
private fun NotificationContent() {
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
                text = stringResource(R.string.notification),
                color = Theme.colors.contentPrimary,
                style = Theme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}