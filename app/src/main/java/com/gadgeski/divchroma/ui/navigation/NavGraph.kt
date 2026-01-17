package com.gadgeski.divchroma.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gadgeski.divchroma.ui.screen.MainScreen
import com.gadgeski.divchroma.ui.theme.NeonEmerald
import com.gadgeski.divchroma.ui.theme.Typography

@Composable
fun DivChromaNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        // === HOME (Cockpit) ===
        composable(Screen.Home.route) {
            MainScreen(
                // MainScreenから設定画面へ遷移する場合の処理などをここに書くことができます
                // 現在のMainScreen実装に合わせて、まずは単純に表示します
            )
        }

        // === SETTINGS (Placeholder) ===
        composable(Screen.Settings.route) {
            // TODO: 本格的なSettingsScreenを作成したら差し替えます
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SYSTEM CONFIG // UNDER CONSTRUCTION",
                    style = Typography.headlineMedium,
                    color = NeonEmerald
                )
            }
        }
    }
}