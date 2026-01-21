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
    private val ignoredSystemFolders = setOf(
        "Alarms",
        "Android",
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

        val targetPath = path.ifEmpty { rootPath }
        val directory = File(targetPath)

        val isRoot = targetPath == rootPath

        if (!directory.exists() || !directory.isDirectory) {
            return@withContext emptyList()
        }

        try {
            directory.listFiles()
                ?.filter { file ->
                    // 1. 隠しファイル (.) を除外
                    // ※削除待機中の .deleted ファイルもここで自動的に除外されます
                    if (file.name.startsWith(".")) return@filter false

                    // 2. ルート階層の場合のみ、不要なシステムフォルダを除外
                    if (isRoot && ignoredSystemFolders.contains(file.name)) return@filter false

                    true
                }
                ?.map { file ->
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
                ?.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() }))
                ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * ファイルまたはディレクトリを物理削除します。
     * ※取り消し不能な操作です。UndoロジックはViewModel側で制御し、
     * 最終的なクリーンアップとしてこのメソッドを使用することを推奨します。
     */
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