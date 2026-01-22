package com.gadgeski.divchroma.ui.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gadgeski.divchroma.data.FileItem
import com.gadgeski.divchroma.data.FileRepository
import com.gadgeski.divchroma.data.SpokeApp
import com.gadgeski.divchroma.domain.usecase.LaunchSpokeAppUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: FileRepository,
    private val launchSpokeAppUseCase: LaunchSpokeAppUseCase
) : ViewModel() {

    // 現在のパス
    private val _currentPath = MutableStateFlow("")
    val currentPath: StateFlow<String> = _currentPath.asStateFlow()

    // 現在選択中のプロジェクトID
    private val _selectedProjectId = MutableStateFlow("proj1")
    val selectedProjectId: StateFlow<String> = _selectedProjectId.asStateFlow()

    // ファイルリスト
    private val _files = MutableStateFlow<List<FileItem>>(emptyList())
    val files: StateFlow<List<FileItem>> = _files.asStateFlow()

    // 検索クエリ
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // ★追加: 隠しファイル表示フラグ
    private val _showHiddenFiles = MutableStateFlow(false)
    val showHiddenFiles: StateFlow<Boolean> = _showHiddenFiles.asStateFlow()

    // スナックバー通知用イベント (One-shot event)
    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent: SharedFlow<String> = _snackbarEvent.asSharedFlow()

    // Undo用の履歴保持 (削除されたファイルの元の名前と、一時退避名を保持)
    private var lastDeletedFile: File? = null
    private var lastDeletedFileTempName: String? = null

    init {
        loadRoot()
    }

    private fun loadRoot() {
        val root = repository.getRootDirectory()
        _currentPath.value = root.absolutePath
        loadFiles(root.absolutePath)
    }

    // ★更新: 隠しファイル設定(_showHiddenFiles.value)を渡してロードするように変更
    private fun loadFiles(path: String) {
        viewModelScope.launch {
            val fileList = repository.getFiles(path, _showHiddenFiles.value)
            _files.value = fileList
        }
    }

    fun navigateTo(path: String) {
        _currentPath.value = path
        loadFiles(path)
    }

    fun navigateUp() {
        val currentFile = File(_currentPath.value)
        val parent = currentFile.parentFile
        if (parent != null && parent.canRead()) {
            navigateTo(parent.absolutePath)
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onProjectSelected(projectId: String) {
        _selectedProjectId.value = projectId
    }

    fun launchSpokeApp(context: Context, app: SpokeApp) {
        launchSpokeAppUseCase(
            context = context,
            app = app,
            projectId = _selectedProjectId.value,
            currentPath = _currentPath.value
        )
    }

    // ★追加: 隠しファイルの表示設定を切り替え
    fun toggleHiddenFiles() {
        _showHiddenFiles.value = !_showHiddenFiles.value
        // 設定変更後に現在のパスでリロードして即座に反映
        loadFiles(_currentPath.value)
    }

    /**
     * ファイルの削除（論理削除）
     * 1. UIから即座に消すために、隠しファイル (.deleted) にリネームする
     * 2. Undo用情報を保持する
     * 3. スナックバーを表示する
     */
    fun deleteFile(fileItem: FileItem) {
        viewModelScope.launch {
            val originalFile = fileItem.file
            val tempName = ".${originalFile.name}.deleted" // 隠しファイル化

            // リポジトリの renameFile を使用
            val success = repository.renameFile(originalFile, tempName)

            if (success) {
                // Undo情報を保存
                lastDeletedFile = File(originalFile.parentFile, tempName)
                lastDeletedFileTempName = originalFile.name // 元の名前

                // リストをリロードしてUI反映 (隠しファイルは自動的に除外される)
                loadFiles(_currentPath.value)

                // スナックバー表示要求
                _snackbarEvent.emit("File deleted")
            } else {
                _snackbarEvent.emit("Failed to delete file")
            }
        }
    }

    /**
     * 直近の削除を元に戻す (Undo)
     */
    fun undoDelete() {
        val targetFile = lastDeletedFile
        val originalName = lastDeletedFileTempName

        if (targetFile != null && originalName != null && targetFile.exists()) {
            viewModelScope.launch {
                // 元の名前にリネームして復元
                val success = repository.renameFile(targetFile, originalName)

                if (success) {
                    loadFiles(_currentPath.value)
                    _snackbarEvent.emit("File restored")
                } else {
                    _snackbarEvent.emit("Failed to restore file")
                }

                // 履歴クリア
                lastDeletedFile = null
                lastDeletedFileTempName = null
            }
        }
    }

    /**
     * ファイルのリネーム
     */
    fun renameFile(fileItem: FileItem, newName: String) {
        viewModelScope.launch {
            val sourceFile = fileItem.file
            if (newName.isBlank()) return@launch

            val success = repository.renameFile(sourceFile, newName)

            if (success) {
                loadFiles(_currentPath.value)
                _snackbarEvent.emit("File renamed")
            } else {
                _snackbarEvent.emit("Failed to rename file")
            }
        }
    }

    /**
     * 新規フォルダの作成
     * FAB (Floating Action Button) から呼び出されます。
     */
    fun createFolder(folderName: String) {
        viewModelScope.launch {
            if (folderName.isBlank()) return@launch

            // Repositoryの createDirectory を呼び出し
            val success = repository.createDirectory(_currentPath.value, folderName)

            if (success) {
                loadFiles(_currentPath.value)
                _snackbarEvent.emit("Folder created")
            } else {
                _snackbarEvent.emit("Failed to create folder")
            }
        }
    }
}