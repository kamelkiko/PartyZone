package com.app.partyzone.seller.ui.screen.auth.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.partyzone.core.util.isNotEmptyAndBlank
import com.app.partyzone.design_system.composable.PzButton
import com.app.partyzone.design_system.composable.PzTextField
import com.app.partyzone.design_system.theme.Theme
import com.app.partyzone.design_system.theme.brush
import com.app.partyzone.seller.R
import com.app.partyzone.seller.ui.navigation.Screen
import com.app.partyzone.seller.ui.util.EventHandler

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = hiltViewModel()) {
    val state by loginViewModel.state.collectAsState()
    val context = LocalContext.current

    EventHandler(effects = loginViewModel.effect) { effect, navController ->
        when (effect) {
            is LoginEffect.ShowToast -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }

            is LoginEffect.NavigateToHome -> {
                navController.navigate(Screen.Home) {
                    popUpTo(Screen.Login) { inclusive = true }
                }
            }

            is LoginEffect.NavigateToSignup -> {
                navController.popBackStack()
                navController.navigate(Screen.Signup)
            }

            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary)
    ) {
        LoginContent(
            email = state.email,
            password = state.password,
            isLoading = state.isLoading,
            errorMessage = state.error,
            onEmailChange = loginViewModel::onEmailChanged,
            onPasswordChange = loginViewModel::onPasswordChanged,
            onClickLogin = loginViewModel::onLogin,
            onClickSignup = loginViewModel::onSignUp,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginContent(
    email: String,
    password: String,
    isLoading: Boolean,
    errorMessage: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onClickLogin: () -> Unit,
    onClickSignup: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = com.app.partyzone.design_system.R.drawable.logo),
            contentDescription = stringResource(R.string.logo),
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = androidx.compose.ui.layout.ContentScale.FillBounds
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = stringResource(R.string.welcome_back),
            color = Theme.colors.contentPrimary,
            style = Theme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.use_credentials_to_access_your_account),
            color = Theme.colors.contentSecondary,
            style = Theme.typography.title,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(30.dp))
        PzTextField(
            hint = stringResource(R.string.enter_email),
            text = email,
            isError = errorMessage.isNullOrEmpty().not(),
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
        Spacer(modifier = Modifier.height(20.dp))
        PzTextField(
            hint = stringResource(R.string.enter_password),
            text = password,
            isError = errorMessage.isNullOrEmpty().not(),
            errorMessage = errorMessage ?: "",
            onValueChange = onPasswordChange,
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
        Spacer(modifier = Modifier.height(30.dp))
        PzButton(
            title = stringResource(R.string.login),
            onClick = onClickLogin,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.CenterHorizontally),
            isLoading = isLoading,
            enabled = email.isNotEmptyAndBlank() && password.isNotEmptyAndBlank(),
        )
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.don_t_have_an_account),
                color = Theme.colors.contentPrimary,
                style = Theme.typography.title,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = stringResource(R.string.signup),
                color = Theme.colors.contentSecondary,
                style = Theme.typography.title.copy(
                    brush = brush
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable { onClickSignup() }
            )
        }
    }
}