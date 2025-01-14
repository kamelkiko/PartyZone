package com.app.partyzone.ui.screen.onboarding.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CenteredContentWithImageAndText(
    imageRes: Int,
    title: String,
    subtitle: String,
    titleStyle: TextStyle,
    subtitleStyle: TextStyle,
    titleColor: androidx.compose.ui.graphics.Color,
    subtitleColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(0.5f)
        )
        Spacer(modifier = Modifier.height(34.dp))
        Text(
            text = title,
            style = titleStyle,
            color = titleColor,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = subtitle,
            style = subtitleStyle,
            color = subtitleColor,
            modifier = Modifier.padding(horizontal = 32.dp),
        )
    }
}