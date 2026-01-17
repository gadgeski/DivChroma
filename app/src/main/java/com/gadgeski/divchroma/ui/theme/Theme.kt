package com.gadgeski.divchroma.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ═══════════════════════════════════════════════════════════════
// DivChroma - Logic Green Cyberpunk Theme
// Always Dark Mode for the Cyberpunk aesthetic
// ═══════════════════════════════════════════════════════════════

private val DivChromaColorScheme = darkColorScheme(
    // Primary colors - Neon Emerald
    primary = NeonEmerald,
    onPrimary = CircuitBackground,
    primaryContainer = DarkMetallicGreen,
    onPrimaryContainer = NeonEmerald,

    // Secondary colors - Metallic Green
    secondary = MalachiteGreen,
    onSecondary = CircuitBackground,
    secondaryContainer = MetallicHighlight,
    onSecondaryContainer = MalachiteGreen,

    // Tertiary colors
    tertiary = CodeFileColor,
    onTertiary = CircuitBackground,

    // Background & Surface - Deep Circuit Black
    background = CircuitBackground,
    onBackground = TextPrimary,
    surface = CircuitSurface,
    onSurface = TextPrimary,
    surfaceVariant = DeepMetallic,
    onSurfaceVariant = TextSecondary,

    // Outline - Use NeonEmeraldDim for subtle borders
    outline = NeonEmeraldDim,
    outlineVariant = InactiveState,

    // Error
    error = ErrorRed,
    onError = Color.White,

    // Inverse
    inverseSurface = TextPrimary,
    inverseOnSurface = CircuitBackground,
    inversePrimary = DarkMetallicGreen
)

/**
 * DivChroma Theme - Always Dark Mode Cyberpunk aesthetic
 * Note: System bar transparency is handled by enableEdgeToEdge() in MainActivity,
 * but icon color needs to be forced to 'light' (white) because the background is always dark.
 */
@Composable
fun DivChromaTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // ここでアイコンの色制御のみ行います。
            // 背景色は enableEdgeToEdge が透明にしてくれますが、
            // アイコンが「黒」にならないように強制的にフラグを操作します。
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = false // false = 白文字 (Dark Theme扱い)
            insetsController.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = DivChromaColorScheme,
        typography = Typography,
        content = content
    )
}