package com.app.partyzone.ui.screen.profile.update

import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.app.partyzone.R
import com.app.partyzone.core.util.isNotEmptyAndBlank
import com.app.partyzone.design_system.composable.PzButton
import com.app.partyzone.design_system.composable.PzCircleImage
import com.app.partyzone.design_system.composable.PzTextField
import com.app.partyzone.design_system.composable.modifier.noRippleEffect
import com.app.partyzone.design_system.theme.Theme
import com.app.partyzone.ui.composable.ErrorView
import com.app.partyzone.ui.util.EventHandler

@Composable
fun UpdateProfileScreen(viewModel: UpdateProfileViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    EventHandler(effects = viewModel.effect) { effect, navController ->
        when (effect) {
            is UpdateProfileEffect.NavigateBack -> {
                navController.popBackStack()
            }

            is UpdateProfileEffect.ShowToast -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary)
    ) {
        AnimatedVisibility(visible = state.isLoadingGetUser) {
            Column(
                Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    color = Theme.colors.contentPrimary,
                )
            }
        }
        AnimatedVisibility(visible = state.isLoadingGetUser.not() && state.error != null) {
            ErrorView(
                error = state.error,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight()
                    .align(Alignment.Center),
                onClickRetry = viewModel::onClickRetry
            )
        }
        AnimatedVisibility(visible = state.isLoadingGetUser.not() && state.error.isNullOrEmpty()) {
            UpdateProfileContent(
                name = state.name,
                email = state.email,
                oldPassword = state.oldPassword,
                newPassword = state.newPassword,
                isLoading = state.isLoading,
                photoUrl = state.photoUrl,
                onClickIconBack = viewModel::onClickIconBack,
                onClickUpdate = viewModel::onClickedUpdate,
                onNameChange = viewModel::onNameChanged,
                onEmailChange = viewModel::onEmailChanged,
                onOldPasswordChange = viewModel::onOldPasswordChanged,
                onNewPasswordChange = viewModel::onNewPasswordChanged,
                onImageUriChange = viewModel::onImageUriChanged,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UpdateProfileContent(
    name: String,
    email: String,
    oldPassword: String,
    newPassword: String,
    isLoading: Boolean,
    photoUrl: String?,
    onClickIconBack: () -> Unit,
    onClickUpdate: () -> Unit,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onOldPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onImageUriChange: (Uri?) -> Unit,
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri = uri
            onImageUriChange(uri)
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
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
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

        AnimatedVisibility(
            (imageUri != null && imageUri.toString()
                .isNotEmpty()) || photoUrl.isNullOrEmpty().not()
        ) {
            AsyncImage(
                model = if (imageUri != null) imageUri else photoUrl ?: "",
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
        AnimatedVisibility(
            (imageUri == null || imageUri.toString().isEmpty()) && photoUrl.isNullOrEmpty()
        ) {
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

        PzTextField(
            hint = stringResource(R.string.enter_username),
            text = name,
            onValueChange = onNameChange,
            keyboardType = KeyboardType.Text,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = com.app.partyzone.design_system.R.drawable.username_icon),
                    contentDescription = stringResource(R.string.username_icon),
                    tint = Theme.colors.contentTertiary
                )
            },
            textFieldModifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )
        PzTextField(
            hint = stringResource(R.string.enter_email),
            text = email,
            onValueChange = onEmailChange,
            keyboardType = KeyboardType.Email,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = com.app.partyzone.design_system.R.drawable.email_icon),
                    contentDescription = stringResource(R.string.email_icon),
                    tint = Theme.colors.contentTertiary
                )
            },
            textFieldModifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )
        PzTextField(
            hint = stringResource(R.string.enter_old_password),
            text = oldPassword,
            onValueChange = onOldPasswordChange,
            keyboardType = KeyboardType.Password,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = com.app.partyzone.design_system.R.drawable.password_icon),
                    contentDescription = stringResource(R.string.password_icon),
                    tint = Theme.colors.contentPrimary
                )
            },
            textFieldModifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )
        PzTextField(
            hint = stringResource(R.string.enter_new_password),
            text = newPassword,
            onValueChange = onNewPasswordChange,
            keyboardType = KeyboardType.Password,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = com.app.partyzone.design_system.R.drawable.password_icon),
                    contentDescription = stringResource(R.string.password_icon),
                    tint = Theme.colors.contentPrimary
                )
            },
            textFieldModifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )
        PzButton(
            title = stringResource(R.string.update),
            onClick = onClickUpdate,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.CenterHorizontally),
            isLoading = isLoading,
            enabled = email.isNotEmptyAndBlank() || name.isNotEmptyAndBlank()
                    || oldPassword.isNotEmptyAndBlank() || newPassword.isNotEmptyAndBlank(),
        )
    }
}