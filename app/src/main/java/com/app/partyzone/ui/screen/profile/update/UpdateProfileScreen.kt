package com.app.partyzone.ui.screen.profile.update

import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.partyzone.R
import com.app.partyzone.design_system.composable.PzCircleImage
import com.app.partyzone.design_system.composable.modifier.noRippleEffect
import com.app.partyzone.design_system.theme.Theme
import com.app.partyzone.ui.util.LocalNavigationProvider

@Composable
fun UpdateProfileScreen() {
    val nav = LocalNavigationProvider.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary)
    ) {
        UpdateProfileContent(
            onClickIconBack = { nav.popBackStack() }
        )
    }
}

@Composable
private fun UpdateProfileContent(
    onClickIconBack: () -> Unit,
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri = uri
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Permission granted, open the gallery
                galleryLauncher.launch("image/*")
            } else {
                // Permission denied, show a message
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(
                painter = painterResource(com.app.partyzone.design_system.R.drawable.arrow_back_icon),
                contentDescription = stringResource(R.string.icon_back),
                tint = Theme.colors.contentPrimary,
                modifier = Modifier
                    .size(24.dp)
                    .noRippleEffect { onClickIconBack() }
            )
            Text(
                text = stringResource(R.string.edit_profile),
                textAlign = TextAlign.Center,
                style = Theme.typography.headline,
                color = Theme.colors.contentPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            )
        }
        Spacer(Modifier.height(32.dp))
        AnimatedVisibility(imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = stringResource(R.string.profile_image),
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .noRippleEffect {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        } else {
                            galleryLauncher.launch("image/*")
                        }
                    },
                placeholder = painterResource(id = com.app.partyzone.design_system.R.drawable.logo),
                error = painterResource(id = com.app.partyzone.design_system.R.drawable.logo),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
            )
        }
        AnimatedVisibility(imageUri == null) {
            PzCircleImage(
                painter = painterResource(id = com.app.partyzone.design_system.R.drawable.logo),
                boxSize = 150.dp,
                imageSize = 150.dp,
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    } else {
                        galleryLauncher.launch("image/*")
                    }
                }
            )
        }
    }
}