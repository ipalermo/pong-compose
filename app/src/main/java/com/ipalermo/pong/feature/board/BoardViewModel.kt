package com.ipalermo.pong.feature.board

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BoardViewModel : ViewModel() {

    private val _boardState = MutableStateFlow(BoardState.Initial)
    val boardState = _boardState.asStateFlow()

    fun update() {
        updateBall()
    }

    fun moveTopRacket(direction: Direction) {
        if (_boardState.value.topRacket.edgeReached(direction)) return

        _boardState.update { state ->
            state.copy(topRacket = state.topRacket.move(direction))
        }
    }

    fun moveBottomRacket(direction: Direction) {
        if (_boardState.value.bottomRacket.edgeReached(direction)) return

        _boardState.update { state ->
            state.copy(bottomRacket = state.bottomRacket.move(direction))
        }
    }

    private fun Racket.edgeReached(direction: Direction) =
        if (direction == Direction.Left && !canMoveLeft) true
        else direction == Direction.Right && !canMoveRight

    private fun Racket.move(direction: Direction) = copy(
        topLeft = topLeft + DpOffset(
            x = if (direction == Direction.Left) speed * -1 else speed,
            y = 0.dp
        )
    )

    fun onBoardMeasured(size: DpSize) = _boardState.update { state ->
        state.copy(
            boardSize = size,
            ball = state.ball.initialise(size),
            topRacket = Racket(boardSize = size),
            bottomRacket = Racket(boardSize = size, topLeftY = size.height - 24.dp),
        )
    }

    private fun Ball.initialise(boardSize: DpSize) = copy(
        position = DpOffset(boardSize.width / 2, boardSize.height / 2)
    )

    private fun updateBall() = _boardState.update { state ->
        val nextDirection = if (state.ballRacketCollision) state.ball.direction.opposite()
            else state.ball.direction
        val newXPos = state.ball.position.x + 1.dp / state.nextSlope
        val nexYPos = state.ball.position.y + if (nextDirection == Direction.Down) state.ball.speed
            else state.ball.speed * -1
        val newPosition = DpOffset(newXPos, nexYPos)
        state.copy(
            ball = state.ball.copy(
                slope = state.nextSlope,
                position = newPosition,
                direction = nextDirection
            )
        )
    }

    private fun BoardState.ballBoardEdgeCollision() = ball.position.x - ball.radius <= 0f.dp
            || ball.position.x + ball.radius >= boardSize.width

    private fun Direction.opposite() = if (this == Direction.Up) Direction.Down else Direction.Up
}

data class BoardState(
    val boardSize: DpSize = DpSize.Zero,
    val status: Status = Status.Playing,
    val ball: Ball = Ball(),
    val topRacket: Racket = Racket(boardSize = boardSize),
    val bottomRacket: Racket = Racket(boardSize = boardSize)
) {
    private val bottomRacketCollision = ball.position.y + ball.radius >= bottomRacket.topLeftY
            && ball.position.x in bottomRacket.topLeft.x..(bottomRacket.topLeft.x + bottomRacket.size.width)

    private val topRacketCollision =
        ball.position.y - ball.radius <= topRacket.topLeftY + topRacket.size.height
                && ball.position.x in topRacket.topLeft.x..(topRacket.topLeft.x + topRacket.size.width)

    private val ballBoardEdgeCollision = ball.position.x - ball.radius <= 0f.dp
    || ball.position.x + ball.radius >= boardSize.width

    val nextSlope = if (bottomRacketCollision) {
        val slopeInclination =
            if (bottomRacket.center.x.value - ball.position.x.value >= 0) -1f else 1f
        val distanceToRacketCenter =
            kotlin.math.abs(bottomRacket.center.x.value - ball.position.x.value)
        if (distanceToRacketCenter <= bottomRacket.size.width.value / 8) 1000f * slopeInclination
        else if (distanceToRacketCenter <= bottomRacket.size.width.value / 4) 1f * slopeInclination
        else 0.25f * slopeInclination
    } else if (topRacketCollision) {
        val slopeInclination =
            if (topRacket.center.x.value - ball.position.x.value >= 0) -1f else 1f
        val distanceToRacketCenter =
            kotlin.math.abs(topRacket.center.x.value - ball.position.x.value)
        if (distanceToRacketCenter <= topRacket.size.width.value / 8) (hitInCenterSlope * slopeInclination)
        else if (distanceToRacketCenter <= topRacket.size.width.value / 4) hitAlmostInCenter * slopeInclination
        else hitOnEdge * slopeInclination
    } else if (ballBoardEdgeCollision) {
        ball.slope * -1
    } else ball.slope

    val ballRacketCollision = bottomRacketCollision || topRacketCollision

    companion object {
        val Initial = BoardState()
        const val hitInCenterSlope = 1000f
        const val hitAlmostInCenter = 1f
        const val hitOnEdge = 0.25f
    }
}

data class Ball(
    val speed: Dp = 10.dp,
    val radius: Dp = 16.dp,
    val slope: Float = 2f,
    val position: DpOffset = DpOffset.Zero,
    val direction: Direction = Direction.Down
)

enum class Direction {
    Right,
    Left,
    Up,
    Down
}

enum class Status {
    Playing,
    Paused,
    Ended
}

data class Racket(
    val speed: Dp = 10.dp,
    val boardSize: DpSize = DpSize.Zero,
    val topLeftY: Dp = 8.dp,
    val size: DpSize = DpSize(
        height = 20.dp,
        width = boardSize.width / 4
    ),
    val topLeft: DpOffset = DpOffset(
        x = boardSize.width / 2 - size.width / 2,
        y = topLeftY
    )
) {
    val center = DpOffset(
        x = topLeft.x + size.width / 2,
        y = topLeftY + size.height / 2
    )
    val canMoveLeft = topLeft.x - 10.dp >= 0.dp
    val canMoveRight = topLeft.x + size.width + 10.dp <= boardSize.width
}