package com.app.partyzone.seller.ui.screen.profile.update

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.partyzone.design_system.theme.Theme

@Composable
fun UpdateProfileScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary)
    ) {
        UpdateProfileContent()
    }
}

@Composable
private fun UpdateProfileContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

    }
}