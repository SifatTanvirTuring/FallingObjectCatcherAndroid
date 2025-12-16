package com.example.catchfallingobjects.game

import java.util.UUID

enum class ObjectType(val emoji: String, val isGood: Boolean) {
    APPLE("🍎", true),
    ORANGE("🍊", true),
    PIZZA("🍕", true),
    BOTTLE("🍼", true),
    WATERMELON("🍉", true),
    GRAPES("🍇", true),
    BANANA("🍌", true),
    BURGER("🍔", true),
    
    BOMB("💣", false),
    KNIFE("🔪", false),
    SKULL("💀", false),
    FIRE("🔥", false)
}

data class FallingObject(
    val id: String = UUID.randomUUID().toString(),
    val type: ObjectType,
    val x: Float,
    val y: Float,
    val speed: Float,
    val size: Float = 1f
)

enum class GameStatus {
    NOT_STARTED,
    PLAYING,
    GAME_OVER
}

data class GameState(
    val status: GameStatus = GameStatus.NOT_STARTED,
    val basketPosition: Float = 0.5f,
    val fallingObjects: List<FallingObject> = emptyList(),
    val caughtGoodItems: Int = 0,
    val lastCaughtBadItem: ObjectType? = null
)
