package com.gadgeski.divchroma.data

import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * FileRepository
 * ファイルシステムへのアクセスを一元管理します。
 * Hilt (@Inject) と Coroutines (suspend) に対応し、メインスレッドをブロックしません。
 */
@Singleton
class FileRepository @Inject constructor() {

    /**
     * ルートディレクトリを取得します
     */
    fun getRootDirectory(): File {
        return Environment.getExternalStorageDirectory()
    }

    /**
     * 指定されたパスのファイル一覧を取得します。
     * 既存コードのロジックを継承しつつ、非同期処理化しています。
     *
     * @param path 対象のディレクトリパス。空文字の場合はルートディレクトリを使用します。
     */
    suspend fun getFiles(path: String = ""): List<FileItem> = withContext(Dispatchers.IO) {
        // 既存コードのロジックを踏襲: 空ならルートへフォールバック
        // Kotlinの ifEmpty 拡張関数を使用してスマートに記述
        val targetPath = path.ifEmpty {
            getRootDirectory().absolutePath
        }

        val directory = File(targetPath)

        // 存在チェック
        if (!directory.exists() || !directory.isDirectory) {
            return@withContext emptyList()
        }

        try {
            directory.listFiles()
                ?.filter { !it.name.startsWith(".") }
                // 隠しファイル除外 (標準的なファイラー挙動)
                ?.map { file ->
                    // FileItemへの変換
                    FileItem(
                        file = file,
                        name = file.name,
                        path = file.absolutePath,
                        isDirectory = file.isDirectory,
                        lastModified = file.lastModified(),
                        size = if (file.isFile) file.length() else 0,
                        extension = file.extension
                    )
                }
                // ソート順: ディレクトリ優先 -> 名前順 (大文字小文字無視)
                ?.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() }))
                ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}