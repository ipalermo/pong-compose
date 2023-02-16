package com.ipalermo.pong.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberPongAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController()
): PongAppState {
    return remember(navController, coroutineScope) {
        PongAppState(navController)
    }
}

@Stable
class PongAppState(
    val navController: NavHostController
) {
    fun onBackClick() {
        navController.popBackStack()
    }
}
