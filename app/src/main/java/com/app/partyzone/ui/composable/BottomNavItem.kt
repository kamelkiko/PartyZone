package com.app.partyzone.ui.composable

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.partyzone.R
import com.app.partyzone.ui.navigation.Screen

sealed class BottomNavItem(
    val route: Screen,
    val icon: @Composable (tint: Brush) -> Unit,
) {
    data object Home : BottomNavItem(
        route = Screen.Home,
        icon = { tint ->
            Icon(
                painter = painterResource(id = com.app.partyzone.design_system.R.drawable.home_icon),
                contentDescription = stringResource(R.string.home),
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(tint, blendMode = BlendMode.SrcAtop)
                        }
                    }
            )
        }
    )

    data object Favourite : BottomNavItem(
        route = Screen.Favourite,
        icon = { tint ->
            Icon(
                painter = painterResource(id = com.app.partyzone.design_system.R.drawable.heart_icon),
                contentDescription = stringResource(R.string.favourite),
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(tint, blendMode = BlendMode.SrcAtop)
                        }
                    }
            )
        },
    )

    data object Party : BottomNavItem(
        route = Screen.Party,
        icon = { tint ->
            Icon(
                painter = painterResource(id = com.app.partyzone.design_system.R.drawable.ticket_icon),
                contentDescription = stringResource(R.string.party),
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(tint, blendMode = BlendMode.SrcAtop)
                        }
                    }
            )
        },
    )

    data object Setting : BottomNavItem(
        route = Screen.Setting,
        icon = { tint ->
            Icon(
                painter = painterResource(id = com.app.partyzone.design_system.R.drawable.setting_icon),
                contentDescription = stringResource(R.string.settings),
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(tint, blendMode = BlendMode.SrcAtop)
                        }
                    }
            )
        },
    )

    data object Profile : BottomNavItem(
        route = Screen.Profile,
        icon = { tint ->
            Icon(
                painter = painterResource(id = com.app.partyzone.design_system.R.drawable.user_icon),
                contentDescription = stringResource(R.string.profile),
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(tint, blendMode = BlendMode.SrcAtop)
                        }
                    }
            )
        },
    )
}