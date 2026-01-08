package com.gadgeski.divchroma.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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
 * System bar colors are handled by enableEdgeToEdge() in MainActivity
 */
@Composable
fun DivChromaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DivChromaColorScheme,
        typography = Typography,
        content = content
    )
}