package com.app.partyzone.ui.screen.search.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.partyzone.R
import com.app.partyzone.design_system.composable.PzCircleImage
import com.app.partyzone.design_system.theme.Theme

@Composable
fun PzSearchCard(
    name: String,
    location: String,
    imageUrl: String?,
    isFavourite: Boolean,
    onClick: () -> Unit,
    onClickFavIcon: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(1.dp, Theme.colors.contentBorder, RoundedCornerShape(Theme.radius.large))
            .clip(RoundedCornerShape(Theme.radius.large))
            .background(Theme.colors.primary, RoundedCornerShape(Theme.radius.large))
            .padding(16.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (imageUrl.isNullOrEmpty().not()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = stringResource(R.string.profile_image),
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                placeholder = painterResource(id = com.app.partyzone.design_system.R.drawable.logo),
                error = painterResource(id = com.app.partyzone.design_system.R.drawable.logo),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
            )
        } else {
            PzCircleImage(
                painter = painterResource(id = com.app.partyzone.design_system.R.drawable.logo),
                boxSize = 64.dp,
                imageSize = 32.dp,
                onClick = {}
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = name,
                    color = Theme.colors.contentPrimary,
                    style = Theme.typography.titleLarge
                )
                Icon(
                    painter = painterResource(id = com.app.partyzone.design_system.R.drawable.heart_icon),
                    contentDescription = stringResource(R.string.heart_icon),
                    tint = if (isFavourite) Color(0xFFFB0160) else Theme.colors.contentSecondary,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onClickFavIcon() }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = location,
                    color = Theme.colors.contentTertiary,
                    style = Theme.typography.title
                )
            }
        }
    }
}