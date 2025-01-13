package com.app.partyzone.design_system.composable

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.app.partyzone.design_system.theme.Theme
import com.app.partyzone.design_system.theme.brush

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@ExperimentalMaterial3Api
@Composable
fun PzButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    enabled: Boolean = true,
    textPadding: PaddingValues = PaddingValues(16.dp),
    shape: Shape = RoundedCornerShape(Theme.radius.large),
    //containerColor: Color = Theme.colors.primary,
    contentColor: Color = Theme.colors.onPrimary,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    isLoading: Boolean = false,
) {
//    val buttonColor by animateColorAsState(
//        if (enabled) containerColor else Theme.colors.disable, label = ""
//    )

    Surface(
        modifier = modifier
            .height(56.dp)
            .background(brush, shape),
        onClick = onClick,
        shape = shape,
        enabled = enabled,
        contentColor = contentColor,
    ) {
        Row(
            if (enabled)
                Modifier
                    .defaultMinSize(
                        minWidth = ButtonDefaults.MinWidth,
                        minHeight = ButtonDefaults.MinHeight
                    )
                    .background(brush, shape)
            else Modifier
                .defaultMinSize(
                    minWidth = ButtonDefaults.MinWidth,
                    minHeight = ButtonDefaults.MinHeight
                )
                .background(Theme.colors.disable, shape),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            painter?.let {
                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp),
                    painter = painter,
                    contentDescription = null
                )
            }
            AnimatedContent(targetState = isLoading, label = "") {
                if (isLoading) PzThreeDotLoadingIndicator()
                else Text(
                    text = title,
                    style = Theme.typography.titleLarge.copy(color = contentColor),
                    modifier = Modifier.padding(textPadding)
                )
            }
        }
    }
}