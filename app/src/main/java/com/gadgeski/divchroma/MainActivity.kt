package com.gadgeski.divchroma

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.gadgeski.divchroma.ui.navigation.DivChromaNavGraph
import com.gadgeski.divchroma.ui.theme.DivChromaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure edge-to-edge with dark system bars for cyberpunk aesthetic
        // 既存コードのこだわり設定を継承：起動直後からステータスバーを強制的にDark扱いにします
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )

        setContent {
            DivChromaTheme {
                // ナビゲーションコントローラーの生成
                val navController = rememberNavController()

                // ここで Scaffold は使いません。
                // 各画面 (MainScreen等) が個別に Scaffold を持つ設計にするため、
                // 親である Activity は純粋なコンテナとして NavGraph を表示するだけにします。
                DivChromaNavGraph(navController = navController)
            }
        }
    }
}