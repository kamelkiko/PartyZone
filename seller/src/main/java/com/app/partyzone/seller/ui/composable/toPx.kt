package com.app.partyzone.seller.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Dp.toPx(): Float = with(LocalDensity.current) { toPx() }