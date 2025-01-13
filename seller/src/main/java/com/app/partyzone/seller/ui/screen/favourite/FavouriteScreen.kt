package com.app.partyzone.seller.ui.screen.favourite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.partyzone.design_system.theme.Theme
import com.app.partyzone.seller.R
import com.app.partyzone.seller.ui.composable.PzCard

@Composable
fun FavouriteScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary)
    ) {
        FavouriteContent()
    }
}

@Composable
private fun FavouriteContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.favourite),
            color = Theme.colors.contentPrimary,
            style = Theme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(32.dp))
        PzCard(modifier = Modifier.padding(bottom = 16.dp))
        PzCard(modifier = Modifier.padding(bottom = 16.dp))
        PzCard(modifier = Modifier.padding(bottom = 16.dp))
        PzCard(modifier = Modifier.padding(bottom = 16.dp))
        PzCard(modifier = Modifier.padding(bottom = 16.dp))
    }
}