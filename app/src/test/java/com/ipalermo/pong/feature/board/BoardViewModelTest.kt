package com.ipalermo.pong.feature.board

import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.ipalermo.pong.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BoardViewModelTest {

    @get:Rule
    val dispatcherRule = MainCoroutineRule()

    private lateinit var viewModel: BoardViewModel

    @Before
    fun setup() {
        viewModel = BoardViewModel()
    }

    @Test
    fun statusIsInitiallyPlaying() = runTest {
        assertEquals(Status.Playing, viewModel.boardState.first().status)
    }

    @Test
    fun ballIsInitiallyCentered() = runTest {
        // Given the board has been measured
        viewModel.onBoardMeasured(DpSize(200.dp, 200.dp))

        val expectedInitialBallPosition = DpOffset(100.dp, 100.dp)
        assertEquals(expectedInitialBallPosition, viewModel.boardState.first().ball.position)
    }

    @Test
    fun bottomRacketIsInitiallyCentered() = runTest {
        // Given the board has been measured
        viewModel.onBoardMeasured(DpSize(200.dp, 200.dp))

        // the bottom racket is centered
        val expectedInitialRacketPosition = DpOffset(75.dp, 176.dp)
        assertEquals(expectedInitialRacketPosition, viewModel.boardState.first().bottomRacket.topLeft)
    }

    @Test
    fun pressingLeftMoveRacketToTheLeft() = runTest {
        // Given the board has been measured
        viewModel.onBoardMeasured(DpSize(200.dp, 200.dp))

        // When player taps on bottom left arrow button
        viewModel.moveBottomRacket(Direction.Left)

        // Then the bottom racket moves left
        val expectedRacketPositionAfterMove = DpOffset(65.dp, 176.dp)
        assertEquals(expectedRacketPositionAfterMove, viewModel.boardState.first().bottomRacket.topLeft)
    }
}