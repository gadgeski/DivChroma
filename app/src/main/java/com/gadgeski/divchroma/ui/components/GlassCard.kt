package com.example.divchroma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.divchroma.ui.theme.ActiveGlow
import com.example.divchroma.ui.theme.DivChromaTheme
import com.example.divchroma.ui.theme.GlassBorder
import com.example.divchroma.ui.theme.GlassHighlight
import com.example.divchroma.ui.theme.GlassSurface
import com.example.divchroma.ui.theme.NeonEmerald
import com.example.divchroma.ui.theme.TextPrimary

/**
 * GlassCard - Glassmorphism styled card component
 * Features:
 * - Semi-transparent dark surface
 * - Gradient border effect (Neon Green to Transparent)
 * - Subtle rounded corners
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    borderWidth: Dp = 0.5.dp,
    cornerRadius: Dp = 8.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    
    // Glass surface with subtle vertical gradient for depth
    // Bio-Synth Glass Surface
    val surfaceBrush = if (isActive) {
        Brush.verticalGradient(
            colors = listOf(
                GlassSurface.copy(alpha = 0.7f),
                GlassSurface.copy(alpha = 0.5f)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                GlassSurface,          // Deep semi-transparent green
                GlassSurface.copy(alpha = 0.8f) // Darker bottom
            )
        )
    }
    
    // Bio-Synth Glass Edge Border
    val borderBrush = if (isActive) {
        Brush.verticalGradient(
            colors = listOf(
                ActiveGlow,
                GlassBorder.copy(alpha = 0.5f),
                Color.Transparent
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                GlassBorder.copy(alpha = 0.8f), // Top: Bright Neon Green (Glass Edge)
                GlassBorder.copy(alpha = 0.2f), // Mid
                Color.Transparent // Bottom: Transparent
            )
        )
    }
    
    Box(
        modifier = modifier
            .clip(shape)
            .background(surfaceBrush)
            .border(
                width = borderWidth,
                brush = borderBrush,
                shape = shape
            )
    ) {
        // Highlight overlay at top for glass reflection
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            if (isActive) GlassHighlight else Color.Transparent,
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = 100f
                    )
                )
        )
        
        // Actual content
        content()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun GlassCardPreview() {
    DivChromaTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                isActive = false
            ) {
                Text(
                    text = "Glass Card Content",
                    color = TextPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun GlassCardActivePreview() {
    DivChromaTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                isActive = true
            ) {
                Text(
                    text = "Active Glass Card",
                    color = NeonEmerald,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
