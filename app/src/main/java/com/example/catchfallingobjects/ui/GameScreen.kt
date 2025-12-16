package com.example.catchfallingobjects.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.catchfallingobjects.game.FallingObject
import com.example.catchfallingobjects.game.GameState
import com.example.catchfallingobjects.game.GameStatus
import com.example.catchfallingobjects.game.GameViewModel
import com.example.catchfallingobjects.ui.theme.ArcadeNeonCyan
import com.example.catchfallingobjects.ui.theme.ArcadeNeonGreen
import com.example.catchfallingobjects.ui.theme.ArcadeNeonPink
import com.example.catchfallingobjects.ui.theme.ArcadeNeonPurple
import com.example.catchfallingobjects.ui.theme.ArcadeNeonYellow
import com.example.catchfallingobjects.ui.theme.BasketColor
import com.example.catchfallingobjects.ui.theme.BasketHighlight
import com.example.catchfallingobjects.ui.theme.GameBackground
import kotlin.random.Random

@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel()
) {
    val gameState by viewModel.gameState.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        GameBackground,
                        Color(0xFF0F1628),
                        Color(0xFF151D35)
                    )
                )
            )
    ) {
        StarfieldBackground()
        
        when (gameState.status) {
            GameStatus.NOT_STARTED -> StartScreen(onStartGame = { viewModel.startGame() })
            GameStatus.PLAYING -> PlayingScreen(gameState = gameState, onMoveBasket = { viewModel.moveBasket(it) })
            GameStatus.GAME_OVER -> GameOverScreen(gameState = gameState, onRestart = { viewModel.startGame() })
        }
    }
}

@Composable
private fun StarfieldBackground() {
    val stars = remember {
        List(50) {
            Triple(
                Random.nextFloat(),
                Random.nextFloat(),
                Random.nextFloat() * 2f + 1f
            )
        }
    }
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        stars.forEach { (x, y, size) ->
            drawCircle(
                color = Color.White.copy(alpha = Random.nextFloat() * 0.5f + 0.2f),
                radius = size,
                center = Offset(x * this.size.width, y * this.size.height)
            )
        }
    }
}

@Composable
private fun StartScreen(onStartGame: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🧺",
            fontSize = 80.sp
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "CATCH",
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Black,
                letterSpacing = 8.sp
            ),
            color = ArcadeNeonCyan
        )
        
        Text(
            text = "FALLING OBJECTS",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            ),
            color = ArcadeNeonPink
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black.copy(alpha = 0.4f))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "HOW TO PLAY",
                style = MaterialTheme.typography.titleMedium,
                color = ArcadeNeonYellow
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "✅ Catch: 🍎 🍊 🍕 🍼 🍉 🍇",
                style = MaterialTheme.typography.bodyLarge,
                color = ArcadeNeonGreen
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "❌ Avoid: 💣 🔪 💀 🔥",
                style = MaterialTheme.typography.bodyLarge,
                color = ArcadeNeonPink
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Drag to move the basket",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = onStartGame,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(60.dp)
                .shadow(8.dp, RoundedCornerShape(30.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = ArcadeNeonCyan
            ),
            shape = RoundedCornerShape(30.dp)
        ) {
            Text(
                text = "▶  START GAME",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                ),
                color = Color.Black
            )
        }
    }
}

@Composable
private fun PlayingScreen(
    gameState: GameState,
    onMoveBasket: (Float) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val newPosition = change.position.x / size.width
                    onMoveBasket(newPosition)
                }
            }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val newPosition = offset.x / size.width
                    onMoveBasket(newPosition)
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            gameState.fallingObjects.forEach { obj ->
                drawFallingObject(obj)
            }
            
            drawBasket(gameState.basketPosition)
        }
        
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "🍎 ${gameState.caughtGoodItems}",
                style = MaterialTheme.typography.titleLarge,
                color = ArcadeNeonGreen
            )
        }
    }
}

private fun DrawScope.drawFallingObject(obj: FallingObject) {
    val x = obj.x * size.width
    val y = obj.y * size.height
    val objectSize = 60f * obj.size
    
    val glowColor = if (obj.type.isGood) {
        Color(0xFF00FF87).copy(alpha = 0.3f)
    } else {
        Color(0xFFFF0000).copy(alpha = 0.3f)
    }
    
    drawCircle(
        color = glowColor,
        radius = objectSize * 1.2f,
        center = Offset(x, y)
    )
    
    val baseColor = if (obj.type.isGood) {
        when (obj.type.emoji) {
            "🍎" -> Color(0xFFFF3B30)
            "🍊" -> Color(0xFFFF9500)
            "🍕" -> Color(0xFFFFCC00)
            "🍼" -> Color(0xFF87CEEB)
            "🍉" -> Color(0xFF4CD964)
            "🍇" -> Color(0xFF9B59B6)
            "🍌" -> Color(0xFFFFE135)
            "🍔" -> Color(0xFFD2691E)
            else -> ArcadeNeonGreen
        }
    } else {
        when (obj.type.emoji) {
            "💣" -> Color(0xFF2C2C2C)
            "🔪" -> Color(0xFFC0C0C0)
            "💀" -> Color(0xFFFFFFFF)
            "🔥" -> Color(0xFFFF4500)
            else -> ArcadeNeonPink
        }
    }
    
    drawCircle(
        color = baseColor,
        radius = objectSize,
        center = Offset(x, y)
    )
    
    drawCircle(
        color = Color.White.copy(alpha = 0.4f),
        radius = objectSize * 0.4f,
        center = Offset(x - objectSize * 0.2f, y - objectSize * 0.2f)
    )
    
    if (!obj.type.isGood) {
        drawCircle(
            color = Color.Red,
            radius = objectSize * 0.3f,
            center = Offset(x, y)
        )
    }
}

private fun DrawScope.drawBasket(position: Float) {
    val basketWidth = size.width * 0.22f
    val basketHeight = 80f
    val basketX = position * size.width
    val basketY = size.height * 0.88f
    
    drawOval(
        color = Color.Black.copy(alpha = 0.3f),
        topLeft = Offset(basketX - basketWidth / 2 + 5, basketY + basketHeight - 10),
        size = Size(basketWidth, 20f)
    )
    
    val basketPath = Path().apply {
        moveTo(basketX - basketWidth / 2 + 15, basketY)
        lineTo(basketX + basketWidth / 2 - 15, basketY)
        lineTo(basketX + basketWidth / 2, basketY + basketHeight)
        lineTo(basketX - basketWidth / 2, basketY + basketHeight)
        close()
    }
    
    drawPath(
        path = basketPath,
        brush = Brush.verticalGradient(
            colors = listOf(BasketHighlight, BasketColor, BasketColor.copy(alpha = 0.8f)),
            startY = basketY,
            endY = basketY + basketHeight
        )
    )
    
    drawOval(
        brush = Brush.horizontalGradient(
            colors = listOf(BasketColor, BasketHighlight, BasketColor)
        ),
        topLeft = Offset(basketX - basketWidth / 2 + 10, basketY - 8),
        size = Size(basketWidth - 20, 20f)
    )
    
    val lineCount = 5
    for (i in 0 until lineCount) {
        val lineY = basketY + (basketHeight / lineCount) * (i + 0.5f)
        val shrinkFactor = 1f - (i.toFloat() / lineCount) * 0.1f
        drawLine(
            color = BasketColor.copy(alpha = 0.6f),
            start = Offset(basketX - (basketWidth / 2 - 5) * shrinkFactor, lineY),
            end = Offset(basketX + (basketWidth / 2 - 5) * shrinkFactor, lineY),
            strokeWidth = 2f
        )
    }
}

@Composable
private fun GameOverScreen(
    gameState: GameState,
    onRestart: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(500),
        label = "scale"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            visible = true,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = gameState.lastCaughtBadItem?.emoji ?: "💣",
                    fontSize = 100.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "GAME OVER",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 4.sp
                    ),
                    color = ArcadeNeonPink
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "You caught a dangerous item!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Good items caught:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "${gameState.caughtGoodItems}",
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = ArcadeNeonGreen
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(48.dp))
                
                Button(
                    onClick = onRestart,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(60.dp)
                        .shadow(8.dp, RoundedCornerShape(30.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ArcadeNeonPurple
                    ),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Text(
                        text = "🔄  PLAY AGAIN",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        ),
                        color = Color.White
                    )
                }
            }
        }
    }
}
