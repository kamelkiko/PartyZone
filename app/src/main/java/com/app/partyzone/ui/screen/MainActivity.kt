package com.app.partyzone.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.app.partyzone.ui.navigation.PZNavHost
import com.app.partyzone.ui.screen.google.GoogleAuthViewModel
import com.app.partyzone.ui.theme.PartyZoneTheme
import com.app.partyzone.ui.util.LocalNavigationProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<GoogleAuthViewModel>()
            val navController = rememberNavController()
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            val signInResult = viewModel.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )

            CompositionLocalProvider(LocalNavigationProvider provides navController) {
                PartyZoneTheme(dynamicColor = false) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        PZNavHost(innerPadding, onGoogleClick = {
                            lifecycleScope.launch(Dispatchers.IO) {
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        viewModel.signIn() ?: return@launch
                                    ).build()
                                )
                            }
                        }
                        )
                    }
                }
            }
        }
    }
}