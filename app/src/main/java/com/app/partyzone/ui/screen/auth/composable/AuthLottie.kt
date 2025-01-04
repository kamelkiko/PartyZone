package com.app.partyzone.ui.screen.auth.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun AuthLottie(
    @androidx.annotation.RawRes resId: Int,
    modifier: Modifier = Modifier,
) {
    val lottieComposition =
        rememberLottieComposition(spec = LottieCompositionSpec.RawRes(resId))

    LottieAnimation(
        composition = lottieComposition.value,
        isPlaying = true,
        iterations = LottieConstants.IterateForever,
        modifier = modifier,
    )
}