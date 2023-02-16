package com.ipalermo.pong.ui

import androidx.compose.runtime.Composable
import com.ipalermo.pong.navigation.PongNavHost

@Composable
fun PongApp(
    appState: PongAppState = rememberPongAppState()
) {
    PongNavHost(
        navController = appState.navController,
        onBackClick = appState::onBackClick
    )
}
