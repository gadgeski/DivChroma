package com.gadgeski.divchroma.domain.usecase

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.gadgeski.divchroma.data.SpokeApp
import javax.inject.Inject

/**
 * LaunchSpokeAppUseCase
 * エコシステム(Spoke App)を起動するためのユースケース。
 * Utils (ContextLinker) にあったロジックをドメイン層へ移動し、
 * DI (Hilt) 可能にすることでテスト容易性とアーキテクチャの整合性を高めました。
 */
class LaunchSpokeAppUseCase @Inject constructor() {

    companion object {
        private const val EXTRA_PROJECT_ID = "EXTRA_PROJECT_ID"
        private const val EXTRA_PROJECT_PATH = "EXTRA_PROJECT_PATH"
    }

    /**
     * 指定されたアプリを起動し、プロジェクトコンテキストを注入します。
     * operator fun invoke を使用することで、関数のように呼び出せます。
     */
    operator fun invoke(
        context: Context,
        app: SpokeApp,
        projectId: String,
        currentPath: String
    ) {
        try {
            val packageManager = context.packageManager
            val launchIntent = packageManager.getLaunchIntentForPackage(app.packageName)

            if (launchIntent != null) {
                // コンテキストの注入 (Context Injection)
                launchIntent.apply {
                    putExtra(EXTRA_PROJECT_ID, projectId)
                    putExtra(EXTRA_PROJECT_PATH, currentPath)
                    // 戻ってきた時にタスクが混ざらないよう、新しいタスクとして起動
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(launchIntent)
            } else {
                // アプリ未インストール時のハンドリング
                Toast.makeText(
                    context,
                    "Linkage Error: ${app.appName} module not found.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                context,
                "System Error: Failed to launch sequence.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}