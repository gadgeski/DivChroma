package com.gadgeski.divchroma.ui.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gadgeski.divchroma.data.FileItem
import com.gadgeski.divchroma.data.FileRepository
import com.gadgeski.divchroma.utils.EcoSystemLauncher // ★ New: ランチャーをインポート
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

/**
 * MainViewModel - Manages the state for the MainScreen
 * Update: Added ecosystem capability to launch BugCodex.
 */
class MainViewModel : ViewModel() {
    private val repository = FileRepository()

    private val _currentPath = MutableStateFlow("")
    val currentPath: StateFlow<String> = _currentPath.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _files = MutableStateFlow(emptyList<FileItem>())
    // filteredFiles depends on _files and _searchQuery
    val files: StateFlow<List<FileItem>> = combine(_files, _searchQuery) { files, query ->
        if (query.isEmpty()) {
            files
        } else {
            files.filter { it.name.contains(query, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadFiles()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    /**
     * Load files from the specified path.
     * If path is empty, it loads from the default external storage root.
     */
    fun loadFiles(path: String = "") {
        viewModelScope.launch {
            _files.value = repository.getFiles(path)
            _currentPath.value = path
        }
    }

    /**
     * Navigate into a directory
     */
    fun navigateTo(path: String) {
        val file = File(path)
        if (file.isDirectory) {
            loadFiles(path)
        }
    }

    /**
     * Navigate up to parent directory
     * Returns true if navigation was successful, false if already at root
     */
    fun navigateUp(): Boolean {
        val current = _currentPath.value
        val rootPath = android.os.Environment.getExternalStorageDirectory().path

        if (current.isEmpty() || current == rootPath) {
            return false
        }

        val parent = File(current).parent ?: return false

        if (rootPath.startsWith(parent) && parent.length < rootPath.length) {
            loadFiles("") // Reset to default root
            return true
        }

        loadFiles(parent)
        return true
    }

    // =================================================================
    // ★ New: EcoSystem (Hub) Functions
    // =================================================================

    /**
     * 現在のコンテキスト（フォルダ）で BugCodex を起動する
     *
     * @param context Activity Context (StartActivityに必要)
     * @param explicitProjectName 任意のプロジェクト名を指定する場合に使用。nullなら現在のフォルダ名を使用。
     */
    fun launchBugCodex(context: Context, explicitProjectName: String? = null) {
        // プロジェクト名の決定ロジック:
        // 1. 引数で指定があればそれを使う
        // 2. なければ現在のパスの「フォルダ名」を使う
        // 3. ルートなどでフォルダ名が取れなければ "Home" とする
        val currentFolder = File(_currentPath.value).name
        val projectName = explicitProjectName ?: if (_currentPath.value.isEmpty()) "Home" else currentFolder

        // ランチャーを起動
        EcoSystemLauncher.launchBugCodex(context, projectName)
    }
}