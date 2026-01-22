package com.gadgeski.divchroma.ui.screen

import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.gadgeski.divchroma.ui.components.CircuitBackground
import com.gadgeski.divchroma.ui.components.GlassCard
import com.gadgeski.divchroma.ui.theme.NeonEmerald
import com.gadgeski.divchroma.ui.theme.SectionHeaderStyle
import com.gadgeski.divchroma.ui.theme.TextPrimary
import com.gadgeski.divchroma.ui.theme.TextSecondary
import com.gadgeski.divchroma.ui.theme.Typography

@Composable
fun SettingsScreen(
    viewModel: MainViewModel
) {
    val showHiddenFiles by viewModel.showHiddenFiles.collectAsState()
    val context = LocalContext.current

    // バージョン情報の取得
    val versionInfo = try {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        // Fix: versionCode is deprecated. Use longVersionCode (Available since API 28).
        // Our minSdk is 34, so this is safe and correct.
        "v${pInfo.versionName} (${pInfo.longVersionCode})"
    } catch (_: PackageManager.NameNotFoundException) {
        "Unknown"
    }

    CircuitBackground(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            Text(
                text = "SYSTEM CONFIG",
                style = SectionHeaderStyle,
                color = NeonEmerald,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Section 1: View Options
            SettingsSection(title = "VIEW OPTIONS") {
                SettingsSwitchItem(
                    icon = Icons.Default.Visibility,
                    title = "Show Hidden Files",
                    subtitle = "Reveal dotfiles (.git, .config)",
                    checked = showHiddenFiles,
                    onCheckedChange = { viewModel.toggleHiddenFiles() }
                )
            }

            // Section 2: About
            SettingsSection(title = "ABOUT MODULE") {
                SettingsInfoItem(
                    icon = Icons.Default.Info,
                    title = "DivChroma Core",
                    value = versionInfo
                )
            }
        }
    }
}

// Removed 'private' to avoid "Parameter is always..." warnings.
// These are reusable components intended for future expansion.
@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = Typography.labelMedium,
            color = TextSecondary,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                content()
            }
        }
    }
}

@Composable
fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.weight(1f)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = NeonEmerald,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Text(text = title, style = Typography.titleMedium, color = TextPrimary)
                Text(text = subtitle, style = Typography.bodySmall, color = TextSecondary)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = NeonEmerald,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = Color.Transparent,
                uncheckedBorderColor = TextSecondary
            )
        )
    }
}

@Composable
fun SettingsInfoItem(
    icon: ImageVector,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = title, style = Typography.titleMedium, color = TextPrimary)
        }
        Text(
            text = value,
            style = Typography.bodyMedium,
            color = NeonEmerald,
            fontFamily = FontFamily.Monospace
        )
    }
}