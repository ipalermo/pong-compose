package com.ipalermo.pong.feature.board

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ipalermo.pong.R

@Composable
internal fun BoardRoute(
    modifier: Modifier = Modifier,
    viewModel: BoardViewModel = hiltViewModel(),
) {
    val boardState by viewModel.boardState.collectAsStateWithLifecycle()

    BoardScreen(
        boardState = boardState,
        modifier = modifier
            .fillMaxSize()
            .background(color = colorScheme.background),
        update = viewModel::update,
        moveTopRacket = viewModel::moveTopRacket,
        moveBottomRacket = viewModel::moveBottomRacket,
        onBoardMeasured = viewModel::onBoardMeasured
    )
}

@VisibleForTesting
@Composable
internal fun BoardScreen(
    boardState: BoardState,
    modifier: Modifier = Modifier,
    update: () -> Unit,
    moveTopRacket: (Direction) -> Unit,
    moveBottomRacket: (Direction) -> Unit,
    onBoardMeasured: (DpSize) -> Unit
) {
    when (boardState.status) {
        Status.Playing -> Board(
            boardState = boardState,
            moveTopRacket = moveTopRacket,
            moveBottomRacket = moveBottomRacket,
            onBoardMeasured = onBoardMeasured
        )
        Status.Paused -> Text(stringResource(R.string.paused))
        Status.Ended -> Text(stringResource(R.string.game_over))
    }
    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos {
                update()
            }
        }
    }
}

@Composable
private fun Board(
    boardState: BoardState,
    moveTopRacket: (Direction) -> Unit,
    moveBottomRacket: (Direction) -> Unit,
    onBoardMeasured: (DpSize) -> Unit
) {
    val topLeftInteractionSource = remember { MutableInteractionSource() }
    val topRightInteractionSource = remember { MutableInteractionSource() }
    val bottomLeftInteractionSource = remember { MutableInteractionSource() }
    val bottomRightInteractionSource = remember { MutableInteractionSource() }

    Column(modifier = Modifier.fillMaxSize()) {
        Controller(
            leftButtonInteractionSource = topLeftInteractionSource,
            rightButtonInteractionSource = topRightInteractionSource
        )

        Board(
            modifier = Modifier.weight(1F),
            state = boardState,
            topLeftInteractionSource = topLeftInteractionSource,
            topRightInteractionSource = topRightInteractionSource,
            bottomLeftInteractionSource = bottomLeftInteractionSource,
            bottomRightInteractionSource = bottomRightInteractionSource,
            moveTopRacket = moveTopRacket,
            moveBottomRacket = moveBottomRacket,
            onBoardMeasured = onBoardMeasured
        )

        Controller(
            leftButtonInteractionSource = bottomLeftInteractionSource,
            rightButtonInteractionSource = bottomRightInteractionSource
        )
    }
}

@Composable
private fun Board(
    modifier: Modifier = Modifier,
    state: BoardState,
    topLeftInteractionSource: MutableInteractionSource,
    topRightInteractionSource: MutableInteractionSource,
    bottomLeftInteractionSource: MutableInteractionSource,
    bottomRightInteractionSource: MutableInteractionSource,
    moveTopRacket: (Direction) -> Unit,
    moveBottomRacket: (Direction) -> Unit,
    onBoardMeasured: (DpSize) -> Unit
) {
    val topLeftPressed by topLeftInteractionSource.collectIsPressedAsState()
    val topRightPressed by topRightInteractionSource.collectIsPressedAsState()
    val bottomLeftPressed by bottomLeftInteractionSource.collectIsPressedAsState()
    val bottomRightPressed by bottomRightInteractionSource.collectIsPressedAsState()
    val density = LocalDensity.current

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .clipToBounds()
            .onSizeChanged {
                with(density) {
                    val boardSize = DpSize(it.width.toDp(), it.height.toDp())
                    onBoardMeasured(boardSize)
                }
            }
    ) {
        drawRacket(
            racketState = state.topRacket,
            leftButtonPressed = topLeftPressed,
            rightButtonPressed = topRightPressed,
            move = moveTopRacket
        )
        drawRacket(
            racketState = state.bottomRacket,
            leftButtonPressed = bottomLeftPressed,
            rightButtonPressed = bottomRightPressed,
            move = moveBottomRacket
        )
        drawBall(state.ball)
    }
}

fun DrawScope.drawRacket(
    racketState: Racket,
    leftButtonPressed: Boolean,
    rightButtonPressed: Boolean,
    move: (Direction) -> Unit
) {
    if (rightButtonPressed) move.invoke(Direction.Right)
    if (leftButtonPressed) move.invoke(Direction.Left)

    drawRect(
        color = Color.Magenta,
        topLeft = Offset(
            x = racketState.topLeft.x.toPx(),
            y = racketState.topLeft.y.toPx()
        ),
        size = Size(racketState.size.width.toPx(), racketState.size.height.toPx())
    )
}

fun DrawScope.drawBall(ballState: Ball) {
    drawCircle(
        color = Color.Cyan,
        radius = ballState.radius.toPx(),
        center = Offset(ballState.position.x.toPx(), ballState.position.y.toPx())
    )
}

@Composable
private fun Controller(
    leftButtonInteractionSource: MutableInteractionSource,
    rightButtonInteractionSource: MutableInteractionSource
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        ControllerButton(
            onClick = {},
            icon = { Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = null) },
            modifier = Modifier.weight(1F),
            interactionSource = leftButtonInteractionSource
        )
        ControllerButton(
            onClick = {},
            icon = { Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null) },
            modifier = Modifier.weight(1F),
            interactionSource = rightButtonInteractionSource
        )
    }
}

@Composable
fun ControllerButton(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        interactionSource = interactionSource
    ) {
        icon()
    }
}

@Preview
@Composable
fun PreviewBoardScreen() {
    BoardScreen(
        boardState = BoardState(),
        modifier = Modifier.fillMaxWidth(),
        update = {},
        moveTopRacket = {},
        moveBottomRacket = {},
        onBoardMeasured = {}
    )
}