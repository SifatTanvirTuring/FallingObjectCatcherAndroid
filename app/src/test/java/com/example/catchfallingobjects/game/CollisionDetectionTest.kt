package com.example.catchfallingobjects.game

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CollisionDetectionTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: GameViewModel

    @org.junit.Before
    fun setUp() {
        viewModel = GameViewModel()
    }

    @Test
    fun `good object caught`() = runTest {
        val fallingObject = FallingObject(
            type = ObjectType.APPLE,
            x = 0.5f,
            y = 0.85f,
            speed = 0.0f
        )
        val initialState = GameState(
            status = GameStatus.PLAYING,
            fallingObjects = listOf(fallingObject)
        )
        viewModel.setGameStateForTest(initialState)

        viewModel.updateGame()

        val newState = viewModel.gameState.value
        assertEquals(1, newState.caughtGoodItems)
        assertEquals(true, newState.fallingObjects.isEmpty())
    }

    @Test
    fun `bad object caught`() = runTest {
        val fallingObject = FallingObject(
            type = ObjectType.BOMB,
            x = 0.5f,
            y = 0.85f,
            speed = 0.0f
        )
        val initialState = GameState(
            status = GameStatus.PLAYING,
            fallingObjects = listOf(fallingObject)
        )
        viewModel.setGameStateForTest(initialState)

        viewModel.updateGame()

        val newState = viewModel.gameState.value
        assertEquals(GameStatus.GAME_OVER, newState.status)
        assertEquals(ObjectType.BOMB, newState.lastCaughtBadItem)
    }

    @Test
    fun `object missed`() = runTest {
        val fallingObject = FallingObject(
            type = ObjectType.APPLE,
            x = 0.8f,
            y = 0.85f,
            speed = 0.0f
        )
        val initialState = GameState(
            status = GameStatus.PLAYING,
            fallingObjects = listOf(fallingObject)
        )
        viewModel.setGameStateForTest(initialState)

        viewModel.updateGame()

        val newState = viewModel.gameState.value
        assertEquals(0, newState.caughtGoodItems)
        assertEquals(GameStatus.PLAYING, newState.status)
        assertEquals(true, newState.fallingObjects.isNotEmpty())
    }

    @Test
    fun `object removed when off-screen`() = runTest {
        val fallingObject = FallingObject(
            type = ObjectType.APPLE,
            x = 0.5f,
            y = 1.2f,
            speed = 0.0f
        )
        val initialState = GameState(
            status = GameStatus.PLAYING,
            fallingObjects = listOf(fallingObject)
        )
        viewModel.setGameStateForTest(initialState)

        viewModel.updateGame()

        val newState = viewModel.gameState.value
        assertEquals(true, newState.fallingObjects.isEmpty())
    }
}