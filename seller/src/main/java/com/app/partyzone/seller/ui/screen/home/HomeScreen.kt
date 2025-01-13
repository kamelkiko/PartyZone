package com.app.partyzone.seller.ui.screen.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.partyzone.design_system.composable.PzNavigationBar
import com.app.partyzone.design_system.composable.PzNavigationBarItem
import com.app.partyzone.design_system.theme.Theme
import com.app.partyzone.design_system.theme.gradientColors

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary)
    ) {
        HomeContent()
    }
}

@Composable
private fun HomeContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

    }
}

//@Composable
//private fun BottomNavigationBarPreview() {
//    var selectedItem by remember { mutableStateOf(3) }
//    val items = listOf("Overview", "Taxis", "Restaurants", "Users", "sds")
//    var xIndicatorOffset by remember { mutableStateOf(Float.NaN) }
//    val xOffsetAnimated by animateFloatAsState(targetValue = xIndicatorOffset, label = "")
//    val indicatorWidthPx = 40.dp.toPx()
//    val iconSizePx = 2.dp.toPx()
//
//    PzNavigationBar(
//        modifier = Modifier
//            .drawTopIndicator(xOffsetAnimated)
//            .padding(WindowInsets.navigationBars.asPaddingValues())
//    ) {
//        items.forEachIndexed { index, item ->
//            PzNavigationBarItem(
//                icon = { tint ->
//                    Icon(
//                        painter = painterResource(com.app.partyzone.design_system.R.drawable.home_icon),
//                        contentDescription = item,
//                        modifier = Modifier
//                            .size(24.dp)
//                            .onGloballyPositioned {
//                                if (selectedItem == index) {
//                                    xIndicatorOffset =
//                                        it.positionInRoot().x + (iconSizePx - indicatorWidthPx) / 2
//                                }
//                            }
//                            .graphicsLayer(alpha = 0.99f)
//                            .drawWithCache {
//                                onDrawWithContent {
//                                    drawContent()
//                                    drawRect(tint, blendMode = BlendMode.SrcAtop)
//                                }
//                            },
//                    )
//                },
//                selected = selectedItem == index,
//                onClick = { selectedItem = index }
//            )
//        }
//    }
//}