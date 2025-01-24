package com.app.partyzone.ui.screen.profile.update

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.partyzone.R
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
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(
                painter = painterResource(com.app.partyzone.design_system.R.drawable.arrow_back_icon),
                contentDescription = stringResource(R.string.icon_back),
            )
            Text(
                text = stringResource(R.string.edit_profile),
                textAlign = TextAlign.Center,
                style = Theme.typography.headline,
                color = Theme.colors.contentPrimary,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}