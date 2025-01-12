package com.app.partyzone.design_system.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.app.partyzone.design_system.R

@Composable
fun headlineLarge(): TextStyle {
    return TextStyle(
        fontSize = 24.sp,
        fontFamily = FontFamily(Font(R.font.poppins_bold)),
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun headline(): TextStyle {
    return TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(R.font.poppins_bold)),
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun titleLarge(): TextStyle {
    return TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(R.font.poppins_regular)),
        fontWeight = FontWeight.Normal,
    )
}

@Composable
fun title(): TextStyle {
    return TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily(Font(R.font.poppins_regular)),
        fontWeight = FontWeight.Normal,
    )
}

@Composable
fun titleMedium(): TextStyle {
    return TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily(Font(R.font.poppins_bold)),
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun body(): TextStyle {
    return TextStyle(
        fontSize = 12.sp,
        fontFamily = FontFamily(Font(R.font.poppins_semi_bold)),
        fontWeight = FontWeight.SemiBold,
    )
}

@Composable
fun caption(): TextStyle {
    return TextStyle(
        fontFamily = FontFamily(Font(R.font.poppins_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
    )
}