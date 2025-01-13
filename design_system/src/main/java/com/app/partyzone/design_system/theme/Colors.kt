package com.app.partyzone.design_system.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class Colors(
    val primary: Color,
    val secondary: Color,
    val contentPrimary: Color,
    val contentSecondary: Color,
    val contentTertiary: Color,
    val contentBorder: Color,
    val surface: Color,
    val onPrimary: Color,
    val hover: Color,
    val background: Color,
    val disable: Color,
    val divider: Color,
    val success: Color,
    val successContainer: Color,
    val warning: Color,
    val warningContainer: Color,
    val surfaceTint: Color,
    val orange: Color,
    val pink: Color,
    val blue: Color,
    val green: Color,
)

val LightColors = Colors(
    primary = Color(0xFFFFFFFF),
    secondary = Color(0xFF25131A),
    contentPrimary = Color(0xFF25131A),
    contentSecondary = Color(0xFF8B8688),
    contentTertiary = Color(0x3325131A),
    contentBorder = Color(0x141F0000),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    hover = Color(0xFFFFFAFA),
    background = Color(0xFFFAFAFA),
    disable = Color(0x401F0000),
    divider = Color(0x141F0000),
    success = Color(0xFF41BE88),
    successContainer = Color(0xFFF0FFF7),
    warning = Color(0xFFF2BD00),
    warningContainer = Color(0xFFFFFCEB),
    surfaceTint = Color(0x081F0000),
    orange = Color(0xFFFFE8CC),
    pink = Color(0xFFFFD0CC),
    blue = Color(0xFFC6E1F7),
    green = Color(0xFFECF6C4),
)

val DarkColors = Colors(
    primary = Color(0xFF25131A),
    secondary = Color(0xFFFFFFFF),
    contentPrimary = Color(0xDEFFFFFF),
    contentSecondary = Color(0x99FFFFFF),
    contentTertiary = Color(0x61FFFFFF),
    contentBorder = Color(0x14FFEFEF),
    surface = Color(0xFF1C1C1C),
    onPrimary = Color(0xFFFFFFFF),
    hover = Color(0xFF242424),
    background = Color(0xFF151515),
    disable = Color(0x401F0000),
    divider = Color(0x14FFFFFF),
    success = Color(0xFF66CB9F),
    successContainer = Color(0x14EBFFF4),
    warning = Color(0xFFCBB567),
    warningContainer = Color(0x14FFFCEB),
    surfaceTint = Color(0x081F0000),
    orange = Color(0xFF26231F),
    pink = Color(0xFF261F1F),
    blue = Color(0xFF1F2326),
    green = Color(0xFF25261E),
)

val gradientColors = listOf(
    Color(0xFFFB0160),
    Color(0xFFF703D0)
)

val brush = Brush.linearGradient(
    colors = gradientColors,
    start = androidx.compose.ui.geometry.Offset(0f, 0f),
    end = androidx.compose.ui.geometry.Offset(1000f, 1000f)
)