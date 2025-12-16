package com.example.catchfallingobjects.game

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GameViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: GameViewModel

    @org.junit.Before
    fun setUp() {
        viewModel = GameViewModel()
    }

    @Test
    fun startGame() = runTest {
        viewModel.startGame()
        val gameState = viewModel.gameState.value
        assertEquals(GameStatus.PLAYING, gameState.status)
        assertEquals(0.5f, gameState.basketPosition)
        assertEquals(0, gameState.caughtGoodItems)
    }

    @Test
    fun moveBasket() = runTest {
        viewModel.startGame()
        viewModel.moveBasket(0.3f)
        assertEquals(0.3f, viewModel.gameState.value.basketPosition)
    }

    @Test
    fun `moveBasket should be clamped to screen boundaries`() = runTest {
        viewModel.startGame()
        viewModel.moveBasket(0.0f)
        assertEquals(0.11f, viewModel.gameState.value.basketPosition, 0.001f)
        viewModel.moveBasket(1.0f)
        assertEquals(0.89f, viewModel.gameState.value.basketPosition, 0.001f)
    }

    @Test
    fun `moveBasket should not work when game is not playing`() = runTest {
        viewModel.moveBasket(0.3f)
        assertEquals(0.5f, viewModel.gameState.value.basketPosition)
    }
}