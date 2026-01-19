package com.gadgeski.divchroma.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * SpokeApp
 * DivChroma (Hub) と連携する Spoke アプリケーションの定義。
 * パッケージ名と表示用メタデータを管理します。
 */
enum class SpokeApp(
    val appName: String,
    val packageName: String,
    val icon: ImageVector,
    val actionLabel: String
) {
    BugCodex(
        appName = "BugCodex",
        packageName = "com.gadgeski.bugcodex",
        icon = Icons.Default.BugReport,
        actionLabel = "BUGS"
    ),
    DailySync(
        appName = "DailySync",
        packageName = "com.gadgeski.dailysync",
        icon = Icons.Default.EditCalendar,
        actionLabel = "REPORT"
    ),
    Abbozzo(
        appName = "Abbozzo",
        packageName = "com.gadgeski.abbozzo",
        icon = Icons.Default.Brush,
        actionLabel = "DRAFT"
    ),
    VetroCodex(
        appName = "VetroCodex",
        packageName = "com.gadgeski.vetro", // パッケージ名は Vetro 由来を想定
        icon = Icons.Default.AccessTime,
        actionLabel = "TIME"
    )
}