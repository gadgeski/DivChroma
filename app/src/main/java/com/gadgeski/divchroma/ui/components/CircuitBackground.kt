package com.gadgeski.divchroma.ui.components

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
import com.gadgeski.divchroma.R
import com.gadgeski.divchroma.ui.theme.DivChromaTheme

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
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF003333), // Brighter Deep Teal (Light Source)
                        Color.Black
                    )
                )
            )
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
        // 1. The Circuit Image Texture - Clean & Bright
        Image(
            painter = painterResource(id = R.drawable.circuit_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 1.0f // Full opacity for the new bright image
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
