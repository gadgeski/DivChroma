package com.example.divchroma.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.divchroma.R
import com.example.divchroma.ui.theme.CircuitBackground
import com.example.divchroma.ui.theme.DivChromaTheme

/**
 * CircuitBackground - PCB (Printed Circuit Board) style background pattern
 * Creates a subtle vertical circuit line pattern overlay
 */
@Composable
fun CircuitBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CircuitBackground)
    ) {
        // Circuit pattern overlay
        CircuitPatternOverlay(
            modifier = Modifier.fillMaxSize()
        )
        
        // Content on top
        content()
    }
}

@Composable
private fun CircuitPatternOverlay(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        // 1. The Circuit Image Texture
        Image(
            painter = painterResource(id = R.drawable.circuit_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.4f // Adjust opacity to blend with dark background
        )

        // 2. Scrim (Gradient)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.9f), // Top: Dark for readability
                            Color.Black.copy(alpha = 0.4f)  // Bottom: Reveal circuit
                        )
                    )
                )
        )

        // 3. The Vignette Effect (Deep Dive)
        // Center is transparent (shows circuit), Edges are solid black
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f),
                            Color.Black
                        ),
                        radius = 1200f // Adjust based on expected screen size or use local density
                    )
                )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun CircuitBackgroundPreview() {
    DivChromaTheme {
        CircuitBackground {
            // Empty for preview
        }
    }
}
