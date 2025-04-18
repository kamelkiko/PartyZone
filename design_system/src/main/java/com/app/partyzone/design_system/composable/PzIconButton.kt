package com.app.partyzone.design_system.composable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.app.partyzone.design_system.theme.Theme

@Composable
fun PzIconButton(
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Int = 56,
    tint: Color = Theme.colors.contentPrimary,
    gapBetweenIconAndContent: Int = 8,
    hasNotifications: Boolean = false,
    content: @Composable (() -> Unit),
) {
    // Shake animation logic
    val shakeAngle by animateFloatAsState(
        targetValue = if (hasNotifications) 10f else 0f, // Target angle for shaking
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 100, // Duration of each shake
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse // Shake back and forth
        ),
        label = "shakeAnimation"
    )


    Surface(
        modifier = modifier
            .height(height.dp)
            .border(
                width = 1.dp, color = Theme.colors.divider, shape = RoundedCornerShape(
                    Theme.radius.medium
                )
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        color = Color.Transparent,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(gapBetweenIconAndContent.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painter,
                contentDescription = "",
                tint = tint,
                modifier = Modifier.rotate(shakeAngle)
            )
            content()
        }
    }
}