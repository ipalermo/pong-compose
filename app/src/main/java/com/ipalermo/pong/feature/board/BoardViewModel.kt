package com.ipalermo.pong.feature.board

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.ipalermo.pong.feature.board.BoardState.Companion.hitAlmostInCenter
import com.ipalermo.pong.feature.board.BoardState.Companion.hitInCenterSlope
import com.ipalermo.pong.feature.board.BoardState.Companion.hitOnEdge
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
        state.copy(
            ball = state.ball.copy(
                slope = state.nextSlope(),
                position = state.nextPosition(state.nextDirection()),
                direction = state.nextDirection()
            )
        )
    }

    // Invert ball direction if it hits a racket
    private fun BoardState.nextDirection() =
        if (ballRacketCollision) ball.direction.opposite() else ball.direction

    /*
    The next position coordinate Y2 in the vertical Y axis is always calculated as the current
    position Y1+1 or Y1-1 depending on the ball direction

    The next position coordinate X2 in the horizontal axis is calculated based on
    the linear equation Y2 - Y1 = m * (X2 - X1), where we know the current position (x1, x2) and the
    slope m, so can we easily calculate X2 = (Y2 - Y1) / m + X1. In our case Y2 - Y1 is always 1
    */
    private fun BoardState.nextPosition(nextDirection: Direction) = DpOffset(
        x = ball.position.x + 1.dp / nextSlope(),
        y = ball.position.y + if (nextDirection == Direction.Down) ball.speed else ball.speed * -1
    )

    // Calculate changes in the slope of ball movement when it hits a racket or the board side edges
    private fun BoardState.nextSlope() =
        if (bottomRacketCollision) slopeAfterRacketHit(bottomRacket)
        else if (topRacketCollision) slopeAfterRacketHit(topRacket)
        else if (ballBoardEdgeCollision) ball.slope * -1
        else ball.slope

    // Calculate changes in the slope of the ball movement based on the position where the ball hits the racket
    private fun BoardState.slopeAfterRacketHit(racket: Racket): Float {

        // If the ball hits the left half of the racket the slope will be descending(negative),
        // If the ball hits the right half of the racket the slope will be positive(ascending)
        val slopeInclination = if (racket.center.x.value - ball.position.x.value >= 0) -1f else 1f

        val distanceToRacketCenter = kotlin.math.abs(racket.center.x.value - ball.position.x.value)

        return when {
            distanceToRacketCenter <= racket.size.width.value / 8 -> hitInCenterSlope * slopeInclination
            distanceToRacketCenter <= racket.size.width.value / 4 -> hitAlmostInCenter * slopeInclination
            else -> hitOnEdge * slopeInclination
        }
    }

    private fun Direction.opposite() = if (this == Direction.Up) Direction.Down else Direction.Up
}

data class BoardState(
    val boardSize: DpSize = DpSize.Zero,
    val status: Status = Status.Playing,
    val ball: Ball = Ball(),
    val topRacket: Racket = Racket(boardSize = boardSize),
    val bottomRacket: Racket = Racket(boardSize = boardSize)
) {
    val bottomRacketCollision = ball.position.y + ball.radius >= bottomRacket.topLeftY
            && ball.position.x in bottomRacket.topLeft.x..(bottomRacket.topLeft.x + bottomRacket.size.width)

    val topRacketCollision =
        ball.position.y - ball.radius <= topRacket.topLeftY + topRacket.size.height
                && ball.position.x in topRacket.topLeft.x..(topRacket.topLeft.x + topRacket.size.width)

    val ballBoardEdgeCollision = ball.position.x - ball.radius <= 0f.dp
            || ball.position.x + ball.radius >= boardSize.width

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