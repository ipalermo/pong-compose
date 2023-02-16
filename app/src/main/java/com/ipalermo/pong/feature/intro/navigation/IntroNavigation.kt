package com.ipalermo.pong.feature.intro.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ipalermo.pong.feature.intro.IntroRoute

const val introRoute = "intro_route"

fun NavGraphBuilder.intro(
    navigateToBoard: () -> Unit
) {
    composable(route = introRoute) {
        IntroRoute(
            navigateToBoard = navigateToBoard,
        )
    }
}
