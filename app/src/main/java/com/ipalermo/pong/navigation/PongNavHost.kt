package com.ipalermo.pong.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ipalermo.pong.feature.board.navigation.board
import com.ipalermo.pong.feature.board.navigation.navigateToBoard
import com.ipalermo.pong.feature.intro.navigation.intro
import com.ipalermo.pong.feature.intro.navigation.introRoute

@Composable
fun PongNavHost(
    navController: NavHostController,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = introRoute
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        intro { navController.navigateToBoard() }
        board()
    }
}
