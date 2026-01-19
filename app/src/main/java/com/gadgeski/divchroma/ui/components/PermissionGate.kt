package com.gadgeski.divchroma.ui.components

import android.content.Intent
import android.os.Environment
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.gadgeski.divchroma.ui.theme.ErrorRed
import com.gadgeski.divchroma.ui.theme.NeonEmerald
import com.gadgeski.divchroma.ui.theme.SectionHeaderStyle
import com.gadgeski.divchroma.ui.theme.TextSecondary
import com.gadgeski.divchroma.ui.theme.Typography

/**
 * PermissionGate
 * 全ファイルアクセス権限(MANAGE_EXTERNAL_STORAGE)をチェックするラッパーコンポーネント。
 * 権限がない場合、サイバーパンク風の「アクセス拒否」画面を表示し、設定画面へ誘導します。
 */
@Composable
fun PermissionGate(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    // DEPRECATION: LocalLifecycleOwnerはlifecycle-runtime-composeへ移動しましたが、
    // 現状の依存関係で動作させるため、意図的に警告を抑制して使用します。
    @Suppress("DEPRECATION")
    val lifecycleOwner = LocalLifecycleOwner.current

    // 権限状態の監視
    var hasPermission by remember {
        mutableStateOf(Environment.isExternalStorageManager())
    }

    // アプリに戻ってきた時(ON_RESUME)に再チェックする
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasPermission = Environment.isExternalStorageManager()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (hasPermission) {
        // 権限があればコンテンツを表示
        content()
    } else {
        // 権限がない場合：認証要求画面
        CircuitBackground {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(16.dp),
                    isActive = true,
                    // 警告状態を示すためアクティブ化
                    borderWidth = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = ErrorRed,
                            modifier = Modifier.size(64.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "ACCESS RESTRICTED",
                            style = SectionHeaderStyle,
                            color = ErrorRed,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "System Root Access Required.\nGrant storage permissions to initialize the orchestrator.",
                            style = Typography.bodyMedium,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        FilledTonalButton(
                            onClick = {
                                try {
                                    // Kotlin Idiom: String.toUri()を使用
                                    val uri = "package:${context.packageName}".toUri()
                                    val intent = Intent(
                                        Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                                        uri
                                    )
                                    context.startActivity(intent)
                                } catch (_: Exception) {
                                    // 一部の機種でパッケージ指定が効かない場合のフォールバック
                                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                                    context.startActivity(intent)
                                }
                            },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = NeonEmerald.copy(alpha = 0.2f),
                                contentColor = NeonEmerald
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.size(8.dp))
                            Text("INITIALIZE PROTOCOL", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}