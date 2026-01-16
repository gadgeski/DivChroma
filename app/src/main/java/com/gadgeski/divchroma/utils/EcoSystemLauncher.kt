package com.gadgeski.divchroma.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri

/**
 * EcoSystemLauncher
 * ガジェスキー・エコシステム内の他アプリを起動するための司令塔ユーティリティ。
 * 各アプリへのDeep Link発行を一元管理します。
 */
object EcoSystemLauncher {

    // BugCodexのパッケージ名（未インストール時のストア誘導などに使用可能）
    private const val PACKAGE_BUGCODEX = "com.gadgeski.bugcodex"

    /**
     * 指定されたプロジェクトのコンテキストで BugCodex を起動する
     *
     * @param context Activity Context
     * @param projectName プロジェクト名（例: "DivChroma", "Abbozzo"）
     * @param repoUrl GitHubのリポジトリURL（任意）
     */
    fun launchBugCodex(context: Context, projectName: String, repoUrl: String? = null) {
        try {
            // URIの構築: bugcodex://open?project=DivChroma
            val builder = "bugcodex://open".toUri().buildUpon()
                .appendQueryParameter("project", projectName)

            if (!repoUrl.isNullOrBlank()) {
                builder.appendQueryParameter("repo", repoUrl)
            }

            val uri = builder.build()

            // Intentの作成
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                // 明示的にフラグをセット
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                // ★ Fix: ここで定数を使用します。
                // これにより「BugCodexというアプリ宛て」であることが確定します。
                setPackage(PACKAGE_BUGCODEX)
            }
            
            // 起動を試みる
            context.startActivity(intent)

        } catch (e: Exception) {
            // アプリが入っていない、または連携に失敗した場合
            e.printStackTrace()
            Toast.makeText(context, "BugCodexの起動に失敗しました: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }
}