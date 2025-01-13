package com.app.partyzone.design_system.composable

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.partyzone.design_system.theme.Theme
import com.app.partyzone.design_system.theme.gradientColors

@Composable
fun PzNavigationBar(
    xOffsetAnimated: Float,
    modifier: Modifier = Modifier,
    navigationBarHeight: Dp = 64.dp,
    backgroundColor: Color = Theme.colors.primary,
    contentColor: Color = contentColorFor(backgroundColor),
    topBorder: Dp = 1.dp,
    borderColor: Color = Theme.colors.divider,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    indicatorWidth: Dp = 40.dp,
    content: @Composable RowScope.() -> Unit,
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        shadowElevation = 0.dp,
        modifier = modifier
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(navigationBarHeight)
                .drawTopIndicator(xOffsetAnimated, indicatorWidth, 2.dp)
//                .drawWithCache {
//                    onDrawWithContent {
//                        drawContent()
//                        // Draw the indicator
//                        if (!xOffsetAnimated.isNaN()) {
//                            drawRect(
//                                color = indicatorColor,
//                                topLeft = Offset(xOffsetAnimated, size.height - 4.dp.toPx()),
//                                size = Size(indicatorWidth.toPx(), 4.dp.toPx()),
//                            )
//                        }
//                    }
//                }
                .selectableGroup()
                .drawBehind {
                    drawRect(
                        color = borderColor,
                        topLeft = Offset(0f, 0f),
                        size = size.copy(height = topBorder.toPx()),
                    )
                },
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(navigationBarHeight),
                horizontalArrangement = horizontalArrangement,
                content = content
            )
        }
    }
}

@Composable
fun RowScope.PzNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable (tint: Brush) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onPositioned: (Offset) -> Unit // Callback to update the indicator position
) {
    val styledIcon = @Composable {
        val iconColor =
            if (selected) Brush.horizontalGradient(gradientColors) else Brush.linearGradient(
                colors = listOf(
                    Theme.colors.contentPrimary,
                    Theme.colors.contentPrimary
                )
            )
        icon(iconColor)
    }

    Box(
        modifier
            .selectable(
                indication = null,
                interactionSource = interactionSource,
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.Tab,
            )
            .selectableGroup()
            .fillMaxHeight()
            .weight(1f)
            .onGloballyPositioned { layoutCoordinates ->
                if (selected) {
                    // Calculate the center position of the item for the indicator
                    val position = layoutCoordinates.positionInRoot()
                    onPositioned(position)
                }
            }
    ) {
        Column(
            modifier = modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            styledIcon()
        }
    }
}

@Composable
private fun Modifier.drawTopIndicator(
    xOffset: Float,
    indicatorWidth: Dp = 40.dp,
    indicatorHeight: Dp = 2.dp,
): Modifier = then(
    Modifier.drawWithContent {
        drawContent()
        drawRoundRect(
            brush = Brush.horizontalGradient(gradientColors),
            topLeft = Offset(xOffset, 0f),
            size = size.copy(width = indicatorWidth.toPx(), height = indicatorHeight.toPx()),
            cornerRadius = CornerRadius(indicatorHeight.toPx() / 2, indicatorHeight.toPx() / 2)
        )
    }
)