package com.example.catchfallingobjects.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel : ViewModel() {
    
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()
    
    private var gameLoopJob: Job? = null
    private var spawnJob: Job? = null
    
    private val frameDelay = 16L
    private val baseSpawnDelay = 1200L
    private val baseFallSpeed = 0.008f
    private val basketWidth = 0.22f
    private val catchZoneTop = 0.82f
    private val catchZoneBottom = 0.95f
    
    private val goodObjects = ObjectType.entries.filter { it.isGood }
    private val badObjects = ObjectType.entries.filter { !it.isGood }
    
    fun startGame() {
        _gameState.update { 
            GameState(
                status = GameStatus.PLAYING,
                basketPosition = 0.5f,
                fallingObjects = emptyList(),
                caughtGoodItems = 0
            )
        }
        startGameLoop()
        startSpawning()
    }
    
    fun moveBasket(position: Float) {
        if (_gameState.value.status == GameStatus.PLAYING) {
            val clampedPosition = position.coerceIn(basketWidth / 2, 1f - basketWidth / 2)
            _gameState.update { it.copy(basketPosition = clampedPosition) }
        }
    }
    
    private fun startGameLoop() {
        gameLoopJob?.cancel()
        gameLoopJob = viewModelScope.launch {
            while (_gameState.value.status == GameStatus.PLAYING) {
                updateGame()
                delay(frameDelay)
            }
        }
    }
    
    private fun startSpawning() {
        spawnJob?.cancel()
        spawnJob = viewModelScope.launch {
            while (_gameState.value.status == GameStatus.PLAYING) {
                spawnObject()
                val spawnDelay = baseSpawnDelay + Random.nextLong(-300, 300)
                delay(spawnDelay.coerceAtLeast(600))
            }
        }
    }
    
    private fun spawnObject() {
        val isGood = Random.nextFloat() < 0.75f
        val objectType = if (isGood) {
            goodObjects.random()
        } else {
            badObjects.random()
        }
        
        val newObject = FallingObject(
            type = objectType,
            x = Random.nextFloat() * 0.8f + 0.1f,
            y = -0.1f,
            speed = baseFallSpeed * (0.8f + Random.nextFloat() * 0.4f),
            size = 0.9f + Random.nextFloat() * 0.2f
        )
        
        _gameState.update { state ->
            state.copy(fallingObjects = state.fallingObjects + newObject)
        }
    }
    
    fun updateGame() {
        val currentState = _gameState.value
        val basketPos = currentState.basketPosition
        val basketLeft = basketPos - basketWidth / 2
        val basketRight = basketPos + basketWidth / 2
        
        var caughtGoodItems = currentState.caughtGoodItems
        var gameOver = false
        var caughtBadItem: ObjectType? = null
        
        val updatedObjects = currentState.fallingObjects.mapNotNull { obj ->
            val newY = obj.y + obj.speed
            
            if (newY >= catchZoneTop && newY <= catchZoneBottom) {
                if (obj.x >= basketLeft && obj.x <= basketRight) {
                    if (obj.type.isGood) {
                        caughtGoodItems++
                    } else {
                        gameOver = true
                        caughtBadItem = obj.type
                    }
                    return@mapNotNull null
                }
            }
            
            if (newY > 1.1f) {
                return@mapNotNull null
            }
            
            obj.copy(y = newY)
        }
        
        if (gameOver) {
            _gameState.update { 
                it.copy(
                    status = GameStatus.GAME_OVER,
                    fallingObjects = updatedObjects,
                    caughtGoodItems = caughtGoodItems,
                    lastCaughtBadItem = caughtBadItem
                )
            }
            gameLoopJob?.cancel()
            spawnJob?.cancel()
        } else {
            _gameState.update { 
                it.copy(
                    fallingObjects = updatedObjects,
                    caughtGoodItems = caughtGoodItems
                )
            }
        }
    }
    
    fun setGameStateForTest(state: GameState) {
        _gameState.value = state
    }
    
    override fun onCleared() {
        super.onCleared()
        gameLoopJob?.cancel()
        spawnJob?.cancel()
    }
}
