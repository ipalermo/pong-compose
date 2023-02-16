package com.ipalermo.pong.feature.board.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ipalermo.pong.feature.board.BoardRoute

const val boardRoute = "board_route"

fun NavController.navigateToBoard(navOptions: NavOptions? = null) {
    this.navigate(boardRoute, navOptions)
}

fun NavGraphBuilder.board() {
    composable(route = boardRoute) {
        BoardRoute()
    }
}
