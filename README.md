# 🧺 Catch Falling Objects

A fun Android game built with Jetpack Compose where you catch falling objects with a basket!

## 🎮 How to Play

- **Drag** or **tap** anywhere on the screen to move the basket left and right
- **Catch** good items: 🍎 🍊 🍕 🍼 🍉 🍇 🍌 🍔
- **Avoid** bad items: 💣 🔪 💀 🔥
- The game ends when you catch a dangerous item!

## 🛠️ Tech Stack

- **Kotlin** - Primary language
- **Jetpack Compose** - Modern declarative UI toolkit
- **Material3** - Material Design 3 components
- **ViewModel** - State management with lifecycle awareness
- **Coroutines & Flow** - Asynchronous game loop

## 📱 Features

- Smooth 60 FPS gameplay
- Touch and drag controls
- Colorful arcade-style visuals
- Animated starfield background
- Visual feedback for good/bad items
- Game over screen with restart option

## 🚀 Getting Started

1. Open the project in Android Studio (Arctic Fox or newer)
2. Sync Gradle files
3. Run on an emulator or physical device (API 24+)

## 📋 Requirements

- Android Studio Arctic Fox (2020.3.1) or newer
- Kotlin 1.9.20
- Android SDK 24+ (target 34)
- JDK 17

## 📁 Project Structure

```
app/src/main/java/com/example/catchfallingobjects/
├── game/
│   ├── GameModels.kt      # Data classes for game objects
│   └── GameViewModel.kt   # Game logic and state management
├── ui/
│   ├── theme/            # Theme colors, typography, shapes
│   └── GameScreen.kt     # Main game UI composable
└── MainActivity.kt        # Entry point
```

## 🎨 Customization

You can easily modify:
- Object types and spawn rates in `GameModels.kt`
- Fall speed and difficulty in `GameViewModel.kt`
- Visual styling in the `ui/theme/` folder

---

Made with ❤️ using Jetpack Compose

