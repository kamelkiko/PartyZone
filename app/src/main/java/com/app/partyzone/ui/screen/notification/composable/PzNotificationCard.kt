package com.app.partyzone.ui.screen.notification.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.partyzone.design_system.composable.PzCircleImage
import com.app.partyzone.design_system.theme.Theme

@Composable
fun PzNotificationCard(
    message: String,
    date: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(1.dp, Theme.colors.contentBorder, RoundedCornerShape(Theme.radius.large))
            .clip(RoundedCornerShape(Theme.radius.large))
            .background(Theme.colors.primary, RoundedCornerShape(Theme.radius.large))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PzCircleImage(
            painter = painterResource(id = com.app.partyzone.design_system.R.drawable.logo),
            boxSize = 64.dp,
            imageSize = 32.dp,
            onClick = {}
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = message,
                color = Theme.colors.contentPrimary,
                style = Theme.typography.titleLarge
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = date,
                    color = Theme.colors.contentTertiary,
                    style = Theme.typography.title
                )
            }
        }
    }
}