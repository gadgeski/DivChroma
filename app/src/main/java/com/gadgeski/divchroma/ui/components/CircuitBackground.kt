package com.gadgeski.divchroma.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gadgeski.divchroma.ui.theme.CircuitBackground
import com.gadgeski.divchroma.ui.theme.NeonEmerald

/**
 * CircuitBackground
 * 画像ファイルを使わず、プログラム描画(Canvas)のみで
 * サイバーパンクなグリッド空間を構築します。
 * Foldableデバイスの画面変形にも完全追従します。
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
                // 既存コードの良い点（グラデーション）を移植
                // 上部を少し明るくすることで空間の広がりを表現
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF001A1A), // ほのかな緑の光源
                        CircuitBackground  // 漆黒
                    )
                )
            )
    ) {
        // グリッドラインの描画 (Canvas)
        // 軽量かつ解像度非依存
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val gridSize = 40.dp.toPx()
            val gridColor = NeonEmerald.copy(alpha = 0.05f) // 極めて薄い緑

            // 縦線
            for (x in 0..canvasWidth.toInt() step gridSize.toInt()) {
                drawLine(
                    color = gridColor,
                    start = Offset(x.toFloat(), 0f),
                    end = Offset(x.toFloat(), canvasHeight),
                    strokeWidth = 1f
                )
            }

            // 横線
            for (y in 0..canvasHeight.toInt() step gridSize.toInt()) {
                drawLine(
                    color = gridColor,
                    start = Offset(0f, y.toFloat()),
                    end = Offset(canvasWidth, y.toFloat()),
                    strokeWidth = 1f
                )
            }
        }

        // コンテンツ（Scaffold等）を上に重ねる
        content()
    }
}