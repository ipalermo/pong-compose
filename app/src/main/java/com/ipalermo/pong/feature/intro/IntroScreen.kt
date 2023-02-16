package com.ipalermo.pong.feature.intro

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ipalermo.pong.ui.theme.PongTheme
import com.ipalermo.pong.R

@Composable
internal fun IntroRoute(
    navigateToBoard: () -> Unit,
    modifier: Modifier = Modifier
) {
    IntroScreen(
        navigateToBoard = navigateToBoard,
        modifier = modifier
    )
}

@Composable
internal fun IntroScreen(
    navigateToBoard: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = navigateToBoard
        ) {
            Text(
                text = stringResource(id = R.string.start)
            )
        }
    }
}

@Preview
@Composable
fun IntroScreen() {
    BoxWithConstraints {
        PongTheme {
            IntroScreen(
                navigateToBoard = {}
            )
        }
    }
}
