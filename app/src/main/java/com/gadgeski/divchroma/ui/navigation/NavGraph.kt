package com.gadgeski.divchroma.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.gadgeski.divchroma.ui.screen.MainScreen
import com.gadgeski.divchroma.ui.screen.MainViewModel
import com.gadgeski.divchroma.ui.screen.SettingsScreen

@Composable
fun DivChromaNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "root", // ネストされたグラフのルート
        modifier = modifier
    ) {
        // ViewModelを共有するためのネストされたナビゲーショングラフ
        navigation(
            startDestination = Screen.Home.route,
            route = "root"
        ) {
            composable(Screen.Home.route) { entry ->
                // 親ルート("root")に関連付けられたViewModelを取得
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry("root")
                }
                val sharedViewModel = hiltViewModel<MainViewModel>(parentEntry)

                MainScreen(viewModel = sharedViewModel)
            }

            composable(Screen.Settings.route) { entry ->
                // 同じく親ルートのViewModelを取得 (これでインスタンスが共有される)
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry("root")
                }
                val sharedViewModel = hiltViewModel<MainViewModel>(parentEntry)

                SettingsScreen(viewModel = sharedViewModel)
            }
        }
    }
}