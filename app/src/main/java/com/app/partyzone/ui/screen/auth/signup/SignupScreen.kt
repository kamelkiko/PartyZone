package com.app.partyzone.ui.screen.auth.signup

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
import com.app.partyzone.ui.screen.auth.composable.AuthCard
import com.app.partyzone.ui.screen.auth.composable.AuthLottie
import com.app.partyzone.ui.util.EventHandler

@Composable
fun SignupScreen(
    innerPadding: PaddingValues,
    onGoogleClick: () -> Unit
) {
    val signupViewModel = hiltViewModel<SignupViewModel>()
    val state by signupViewModel.state.collectAsState()
    val context = LocalContext.current

    EventHandler(effects = signupViewModel.effect) { effect, navController ->
        when (effect) {
            is SignupEffect.ShowToast -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }

            is SignupEffect.NavigateToHome -> {
                navController.navigate(Home) {
                    popUpTo(Login) { inclusive = true }
                }
            }

            is SignupEffect.NavigateToLogin -> {
                navController.navigate(Login)
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
                .fillMaxHeight(0.2f)
        )
        AuthCard(
            title = stringResource(R.string.let_s_create_your_account),
            description = stringResource(R.string.create_a_new_account_using_your_credentials),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp)
                .weight(1f),
            email = state.email,
            password = state.password,
            username = state.userName,
            onEmailChange = signupViewModel::onEmailChanged,
            onPasswordChange = signupViewModel::onPasswordChanged,
            onUserNameChange = signupViewModel::onUserNameChanged,
            signText = stringResource(R.string.sign_up),
            onGoogleClick = onGoogleClick,
            onSignButtonClick = signupViewModel::onSingUp,
            onSignTextClick = signupViewModel::onSignIn
        )
    }
}