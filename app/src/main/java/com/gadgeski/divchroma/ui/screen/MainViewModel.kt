package com.gadgeski.divchroma.ui.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gadgeski.divchroma.data.FileItem
import com.gadgeski.divchroma.data.FileRepository
import com.gadgeski.divchroma.data.SpokeApp
import com.gadgeski.divchroma.utils.ContextLinker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: FileRepository
) : ViewModel() {

    // 現在のパス
    private val _currentPath = MutableStateFlow("")
    val currentPath: StateFlow<String> = _currentPath.asStateFlow()

    // 現在選択中のプロジェクトID (初期値は仮)
    private val _selectedProjectId = MutableStateFlow("proj1")
    val selectedProjectId: StateFlow<String> = _selectedProjectId.asStateFlow()

    // ファイルリスト
    private val _files = MutableStateFlow<List<FileItem>>(emptyList())
    val files: StateFlow<List<FileItem>> = _files.asStateFlow()

    // 検索クエリ
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadRoot()
    }

    private fun loadRoot() {
        val root = repository.getRootDirectory()
        _currentPath.value = root.absolutePath
        loadFiles(root.absolutePath)
    }

    private fun loadFiles(path: String) {
        viewModelScope.launch {
            val fileList = repository.getFiles(path)
            _files.value = fileList
        }
    }

    fun navigateTo(path: String) {
        _currentPath.value = path
        loadFiles(path)
    }

    fun navigateUp() {
        val currentFile = java.io.File(_currentPath.value)
        val parent = currentFile.parentFile
        if (parent != null && parent.canRead()) {
            navigateTo(parent.absolutePath)
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    /**
     * プロジェクト選択の更新
     */
    fun onProjectSelected(projectId: String) {
        _selectedProjectId.value = projectId
        // 将来的に、プロジェクト選択と同時にそのルートディレクトリへジャンプするロジックをここに追加可能
    }

    /**
     * EcoSystem連携: 指定されたSpokeアプリを起動
     */
    fun launchSpokeApp(context: Context, app: SpokeApp) {
        ContextLinker.launch(
            context = context,
            app = app,
            projectId = _selectedProjectId.value,
            currentPath = _currentPath.value
        )
    }
}