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
     * Update: showHidden フラグを追加
     *
     * @param path 対象のディレクトリパス
     * @param showHidden 隠しファイル(.から始まるファイル)を表示するかどうか
     */
    suspend fun getFiles(path: String = "", showHidden: Boolean = false): List<FileItem> = withContext(Dispatchers.IO) {
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
                    // 1. 隠しファイル (.) の制御
                    // showHiddenがfalse かつ .で始まる場合は除外
                    if (!showHidden && file.name.startsWith(".")) return@filter false

                    // 2. ルート階層の場合のみ、不要なシステムフォルダを除外
                    // (これはシステム設定なのでshowHiddenに関わらず常に除外します)
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

    // ... createDirectory, deleteFile, renameFile (変更なし) ...
    /**
     * 新規ディレクトリを作成します。
     */
    suspend fun createDirectory(parentPath: String, folderName: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val parent = File(parentPath)
            if (!parent.exists() || !parent.isDirectory) return@withContext false

            val newDir = File(parent, folderName)
            if (newDir.exists()) return@withContext false

            newDir.mkdirs()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

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