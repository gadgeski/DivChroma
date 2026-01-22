package com.gadgeski.divchroma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gadgeski.divchroma.ui.theme.CircuitSurface
import com.gadgeski.divchroma.ui.theme.GlassBorder
import com.gadgeski.divchroma.ui.theme.NeonEmerald
import com.gadgeski.divchroma.ui.theme.SectionHeaderStyle
import com.gadgeski.divchroma.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenameDialog(
    initialName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    // 拡張子を除いた部分を選択状態にするためのロジック
    val nameWithoutExtension = initialName.substringBeforeLast('.', initialName)
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = initialName,
                selection = TextRange(0, nameWithoutExtension.length)
            )
        )
    }

    val focusRequester = remember { FocusRequester() }

    BasicAlertDialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(CircuitSurface)
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(NeonEmerald.copy(alpha = 0.5f), Color.Transparent)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "RENAME SEQUENCE",
                style = SectionHeaderStyle,
                color = NeonEmerald,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = NeonEmerald,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = NeonEmerald,
                    unfocusedBorderColor = GlassBorder,
                    cursorColor = NeonEmerald
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            FilledTonalButton(
                onClick = { onConfirm(textFieldValue.text) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = NeonEmerald.copy(alpha = 0.2f),
                    contentColor = NeonEmerald
                )
            ) {
                Text("EXECUTE", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("CANCEL", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }

    // ダイアログ表示時に自動フォーカス
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}