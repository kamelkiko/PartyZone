package com.app.partyzone.seller.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.partyzone.design_system.composable.PzNavigationBar
import com.app.partyzone.design_system.composable.PzNavigationBarItem
import com.app.partyzone.design_system.theme.PzTheme
import com.app.partyzone.seller.ui.composable.BottomNavItem
import com.app.partyzone.seller.ui.navigation.AppNavHost
import com.app.partyzone.seller.ui.util.LocalNavigationProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PzTheme {
                val navController = rememberNavController()
                CompositionLocalProvider(LocalNavigationProvider provides navController) {
                    val currentRoute =
                        navController.currentBackStackEntryAsState().value?.destination?.route
                    val bottomNavScreens = listOf(
                        "com.app.partyzone.seller.ui.navigation.Screen.Home",
                        "com.app.partyzone.seller.ui.navigation.Screen.Party",
                        "com.app.partyzone.seller.ui.navigation.Screen.Setting",
                        "com.app.partyzone.seller.ui.navigation.Screen.Profile"
                    )
                    var selectedItem by remember { mutableIntStateOf(0) }
                    var xIndicatorOffset by remember { mutableFloatStateOf(Float.NaN) }
                    val xOffsetAnimated by animateFloatAsState(
                        targetValue = xIndicatorOffset,
                        label = ""
                    )
                    val indicatorWidth = 40.dp
                    val density = LocalDensity.current
                    val indicatorWidthPx = with(density) { 48.dp.toPx() }
                    val iconSizePx = with(density) { 24.dp.toPx() }
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            if (bottomNavScreens.contains(currentRoute ?: "")) {
                                PzNavigationBar(
                                    xOffsetAnimated = xOffsetAnimated,
                                    indicatorWidth = indicatorWidth,
                                    modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues())
                                ) {
                                    listOf(
                                        BottomNavItem.Home,
                                        BottomNavItem.Party,
                                        BottomNavItem.Setting,
                                        BottomNavItem.Profile
                                    ).forEachIndexed { index, item ->
                                        PzNavigationBarItem(
                                            icon = item.icon,
                                            selected = selectedItem == index,
                                            onClick = {
                                                selectedItem = index
                                                navController.navigate(item.route) {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            },
                                            onPositioned = { position ->
                                                xIndicatorOffset =
                                                    position.x + (indicatorWidthPx - iconSizePx)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    ) { innerPadding ->
                        AppNavHost(innerPadding)
                    }
                }
            }
        }
    }
}