package com.app.partyzone.seller.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.app.partyzone.design_system.composable.PzButton
import com.app.partyzone.design_system.composable.PzOutlinedButton
import com.app.partyzone.design_system.composable.PzRoundedImage
import com.app.partyzone.design_system.theme.Theme
import com.app.partyzone.design_system.theme.brush
import com.app.partyzone.seller.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PzCard(
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
        PzRoundedImage(
            painter = painterResource(id = com.app.partyzone.design_system.R.drawable.logo),
            modifier = Modifier.size(64.dp),
            onClick = {}
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Dance party at the top of the town - 2022", // seller name
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
                    text = "New York", // location
                    color = Theme.colors.contentTertiary,
                    style = Theme.typography.title
                )
                Spacer(modifier = Modifier.weight(1f))
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
                        text = "$30.00", // price
                        style = Theme.typography.title.copy(brush = brush),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                PzButton(title = "Cancel Booking", onClick = { /*TODO*/ })
                PzButton(title = "View Ticket", onClick = { })
            }
        }
    }
}