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

    // 除外するシステムフォルダのリスト（ルート階層のみ適用）
    // 開発に関係のない標準フォルダや、アクセス制限のあるシステムフォルダを除外して
    // コックピットの視認性を最大化します。
    private val ignoredSystemFolders = setOf(
        "Alarms",
        "Android",      // System data (Restricted access & High risk)
        "Audiobooks",
        "Movies",
        "Music",
        "Notifications",
        "Podcasts",
        "Recordings",
        "Ringtones"
    )

    /**
     * ルートディレクトリを取得します
     */
    fun getRootDirectory(): File {
        return Environment.getExternalStorageDirectory()
    }

    /**
     * 指定されたパスのファイル一覧を取得します。
     *
     * @param path 対象のディレクトリパス。空文字の場合はルートディレクトリを使用します。
     */
    suspend fun getFiles(path: String = ""): List<FileItem> = withContext(Dispatchers.IO) {
        val rootFile = getRootDirectory()
        val rootPath = rootFile.absolutePath

        // パスが空ならルートを使用
        val targetPath = path.ifEmpty { rootPath }
        val directory = File(targetPath)

        // 現在ルートディレクトリを見ているか判定
        val isRoot = targetPath == rootPath

        // 存在チェック
        if (!directory.exists() || !directory.isDirectory) {
            return@withContext emptyList()
        }

        try {
            directory.listFiles()
                ?.filter { file ->
                    // 1. 隠しファイル (.) を除外
                    if (file.name.startsWith(".")) return@filter false

                    // 2. ルート階層の場合のみ、不要なシステムフォルダを除外
                    if (isRoot && ignoredSystemFolders.contains(file.name)) return@filter false

                    true
                }
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

    /**
     * 新規ディレクトリを作成します。
     */
    suspend fun createDirectory(parentPath: String, folderName: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val parent = File(parentPath)
            if (!parent.exists() || !parent.isDirectory) return@withContext false

            val newDir = File(parent, folderName)
            if (newDir.exists()) return@withContext false // 既に存在する場合は失敗

            newDir.mkdirs()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * ファイルまたはディレクトリを物理削除します。
     * ※現在は論理削除(リネーム)を優先しているため未使用ですが、
     * 将来的な「完全削除」機能のために保持します。
     */
    @Suppress("unused")
    suspend fun deleteFile(file: File): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            if (file.isDirectory) {
                file.deleteRecursively()
            } else {
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * ファイルのリネーム（移動）を行います。
     * Undo機能の実装（一時的な隠しファイルへの退避）にも使用します。
     */
    suspend fun renameFile(source: File, newName: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val destination = File(source.parentFile, newName)
            source.renameTo(destination)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}