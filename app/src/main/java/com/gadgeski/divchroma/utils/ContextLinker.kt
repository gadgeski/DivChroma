package com.gadgeski.divchroma.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.gadgeski.divchroma.data.SpokeApp

/**
 * ContextLinker
 * 現在のコンテキスト(Project, Path)をIntentに注入し、
 * Spokeアプリを起動するためのユーティリティクラス。
 */
object ContextLinker {

    private const val EXTRA_PROJECT_ID = "EXTRA_PROJECT_ID"
    private const val EXTRA_PROJECT_PATH = "EXTRA_PROJECT_PATH"

    /**
     * 指定されたSpokeアプリを、コンテキスト付きで起動します。
     */
    fun launch(
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
                // アプリが見つからない場合
                // 将来的にはPlay Storeへの誘導などが考えられます
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