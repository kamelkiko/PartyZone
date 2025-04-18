package com.app.partyzone.seller.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.partyzone.design_system.composable.PzButton
import com.app.partyzone.design_system.composable.PzCircleImage
import com.app.partyzone.design_system.composable.PzRoundedImage
import com.app.partyzone.design_system.theme.Theme
import com.app.partyzone.design_system.theme.brush
import com.app.partyzone.seller.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PzFavouriteCard(
    name: String,
    location: String,
    imageUrl: String?,
    price: Double?,
    modifier: Modifier = Modifier,
    isUpcoming: Boolean = false,
    id: String = "",
    onClick: () -> Unit,
    onClickAccept: (String) -> Unit = {},
    onClickCancel: (String) -> Unit = {},
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
                    .size(64.dp)
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
            Text(
                text = name,
                color = Theme.colors.contentPrimary,
                style = Theme.typography.titleLarge
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = com.app.partyzone.design_system.R.drawable.location_icon),
                    contentDescription = stringResource(R.string.location_icon),
                    tint = Theme.colors.contentSecondary,
                    modifier = Modifier
                        .size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = location,
                    color = Theme.colors.contentTertiary,
                    style = Theme.typography.title
                )
                Spacer(modifier = Modifier.weight(1f))
                AnimatedVisibility(visible = price != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(Theme.radius.large))
                            .background(
                                color = Color(0xFFFB0160).copy(alpha = 0.1f),
                                RoundedCornerShape(Theme.radius.large)
                            )
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "$" + price.toString(),
                            style = Theme.typography.title.copy(brush = brush),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            if (isUpcoming) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PzButton("Accept", { onClickAccept(id) })
                    Spacer(modifier = Modifier.width(16.dp))
                    PzButton("Cancel", { onClickCancel(id) })
                }
            }
        }
    }
}