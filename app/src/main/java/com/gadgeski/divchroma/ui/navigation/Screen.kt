package com.gadgeski.divchroma.ui.navigation

/**
 * Screen Route Definitions
 * アプリ内の画面遷移先を一元管理します。
 */
sealed class Screen(val route: String) {
    // コックピット（メイン画面）
    data object Home : Screen("home")

    // 設定画面
    data object Settings : Screen("settings")

    // 将来的な拡張例:
    // object ProjectDetail : Screen("project/{projectId}") {
    //     fun createRoute(projectId: String) = "project/$projectId"
    // }
}