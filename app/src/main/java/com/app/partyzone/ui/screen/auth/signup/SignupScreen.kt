package com.app.partyzone.ui.screen.auth.signup

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
import com.app.partyzone.R
import com.app.partyzone.core.util.isNotEmptyAndBlank
import com.app.partyzone.design_system.composable.PzButton
import com.app.partyzone.design_system.composable.PzTextField
import com.app.partyzone.design_system.theme.Theme
import com.app.partyzone.design_system.theme.brush
import com.app.partyzone.ui.navigation.Screen
import com.app.partyzone.ui.util.EventHandler

@Composable
fun SignupScreen(signupViewModel: SignupViewModel = hiltViewModel()) {
    val state by signupViewModel.state.collectAsState()
    val context = LocalContext.current

    EventHandler(effects = signupViewModel.effect) { effect, navController ->
        when (effect) {
            is SignupEffect.ShowToast -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }

            is SignupEffect.NavigateToHome -> {
                navController.navigate(Screen.Home) {
                    popUpTo(Screen.Login) { inclusive = true }
                }
            }

            is SignupEffect.NavigateToLogin -> {
                navController.popBackStack()
                navController.navigate(Screen.Login)
            }

            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.primary)
    ) {
        SignupContent(
            email = state.email,
            password = state.password,
            name = state.userName,
            isLoading = state.isLoading,
            errorMessage = state.error,
            onEmailChange = signupViewModel::onEmailChanged,
            onPasswordChange = signupViewModel::onPasswordChanged,
            onNameChange = signupViewModel::onUserNameChanged,
            onClickSignup = signupViewModel::onSingUp,
            onClickLogin = signupViewModel::onLogIn,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignupContent(
    email: String,
    password: String,
    name: String,
    isLoading: Boolean,
    errorMessage: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onClickSignup: () -> Unit,
    onClickLogin: () -> Unit,
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
            hint = stringResource(R.string.enter_username),
            text = name,
            isError = errorMessage.isNullOrEmpty().not(),
            onValueChange = onNameChange,
            keyboardType = KeyboardType.Text,
            leadingIcon = {
                Icon(
                    painter = painterResource(com.app.partyzone.design_system.R.drawable.username_icon),
                    contentDescription = stringResource(R.string.username_icon),
                    tint = Theme.colors.contentPrimary
                )
            },
            textFieldModifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        PzTextField(
            hint = stringResource(R.string.enter_email),
            text = email,
            onValueChange = onEmailChange,
            isError = errorMessage.isNullOrEmpty().not(),
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
            onValueChange = onPasswordChange,
            isError = errorMessage.isNullOrEmpty().not(),
            errorMessage = errorMessage ?: "",
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
            title = stringResource(R.string.signup),
            onClick = onClickSignup,
            isLoading = isLoading,
            enabled = email.isNotEmptyAndBlank() && password.isNotEmptyAndBlank() && name.isNotEmptyAndBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.do_have_an_account),
                color = Theme.colors.contentPrimary,
                style = Theme.typography.title,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = stringResource(R.string.login),
                color = Theme.colors.contentSecondary,
                style = Theme.typography.title.copy(
                    brush = brush
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable { onClickLogin() }
            )
        }
    }
}