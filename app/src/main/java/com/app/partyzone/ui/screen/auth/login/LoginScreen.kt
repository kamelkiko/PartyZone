package com.app.partyzone.ui.screen.auth.login

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.partyzone.R
import com.app.partyzone.ui.navigation.Home
import com.app.partyzone.ui.navigation.Login
import com.app.partyzone.ui.navigation.Signup
import com.app.partyzone.ui.screen.auth.composable.AuthCard
import com.app.partyzone.ui.screen.auth.composable.AuthLottie
import com.app.partyzone.ui.util.EventHandler

@Composable
fun LoginScreen(
    innerPadding: PaddingValues,
    onGoogleClick: () -> Unit,
) {
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val state by loginViewModel.state.collectAsState()
    val context = LocalContext.current

    EventHandler(effects = loginViewModel.effect) { effect, navController ->
        when (effect) {
            is LoginEffect.ShowToast -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }

            is LoginEffect.NavigateToHome -> {
                navController.navigate(Home) {
                    popUpTo(Login) { inclusive = true }
                }
            }

            is LoginEffect.NavigateToSignup -> {
                navController.navigate(Signup)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        AuthLottie(
            resId = R.raw.login_anim, modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.27f)
        )

        AuthCard(
            title = stringResource(R.string.let_s_get_you_signed_in),
            description = stringResource(R.string.sign_in_using_credentials_or_your_google_account),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp)
                .weight(1f),
            email = state.email,
            password = state.password,
            username = null,
            onEmailChange = loginViewModel::onEmailChanged,
            onPasswordChange = loginViewModel::onPasswordChanged,
            onUserNameChange = {},
            signText = stringResource(R.string.sign_in),
            onSignButtonClick = loginViewModel::onLogin,
            onSignTextClick = loginViewModel::onSignUp,
            onGoogleClick = onGoogleClick,
        )
    }
}