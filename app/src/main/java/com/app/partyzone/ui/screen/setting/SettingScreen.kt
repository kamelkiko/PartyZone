package com.app.partyzone.ui.screen.setting

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.partyzone.R
import com.app.partyzone.design_system.composable.PzButton
import com.app.partyzone.design_system.composable.PzOutlinedButton
import com.app.partyzone.design_system.theme.Theme
import com.app.partyzone.ui.navigation.Screen
import com.app.partyzone.ui.util.EventHandler

@Composable
fun SettingScreen(viewModel: SettingViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    EventHandler(effects = viewModel.effect) { effect, navController ->
        when (effect) {
            is SettingEffect.NavigateToEditProfile -> {
                // navController.navigate(Screen.Notification)
            }

            SettingEffect.NavigateToLogin -> {
                navController.navigate(Screen.Login) {
                    popUpTo<Screen.Setting> {
                        inclusive = true
                    }
                }
            }

            is SettingEffect.ShowToast -> {
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
        SettingContent(
            isLoading = state.isLoading,
            onClickLogout = viewModel::onClickedLogout,
            onClickEditProfile = viewModel::onClickedEditProfile,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingContent(
    isLoading: Boolean,
    onClickLogout:()->Unit,
    onClickEditProfile:()->Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        PzButton(
            stringResource(R.string.update_profile),
            onClick = onClickEditProfile,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            isLoading = isLoading,
            enabled = isLoading.not(),
        )
        Spacer(Modifier.height(32.dp))
        PzOutlinedButton(
            stringResource(R.string.log_out),
            onClick = onClickLogout,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            enabled = isLoading.not(),
        )
    }
}