package com.app.partyzone.seller.ui.screen.onboarding.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.partyzone.design_system.theme.Theme
import com.app.partyzone.design_system.theme.gradientColors

@Composable
fun GradientPagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    activeColor: List<Color> = gradientColors,
    inactiveColor: Color = Theme.colors.contentSecondary,
    dotSize: Dp = 28.dp,
    spacing: Dp = 2.dp,
) {
    val dotCount = pagerState.pageCount
    val currentPage = pagerState.currentPage

    Row(
        modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(dotCount) { iteration ->
            val color =
                if (currentPage == iteration) Brush.horizontalGradient(colors = activeColor)
                else
                    Brush.linearGradient(colors = listOf(inactiveColor, inactiveColor))
            Box(
                modifier = Modifier
                    .padding(spacing)
                    .clip(if (currentPage == iteration) RoundedCornerShape(Theme.radius.large) else CircleShape)
                    .background(color)
                    .width(if (currentPage == iteration) dotSize else 4.dp)
                    .height(4.dp)
            )
        }
    }
}