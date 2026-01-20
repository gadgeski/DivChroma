package com.gadgeski.divchroma.ui.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gadgeski.divchroma.data.FileItem
import com.gadgeski.divchroma.data.FileRepository
import com.gadgeski.divchroma.data.SpokeApp
import com.gadgeski.divchroma.domain.usecase.LaunchSpokeAppUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: FileRepository,
    // Inject: UseCaseを注入（ContextLinkerへの依存を排除）
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
    }

    /**
     * EcoSystem連携: UseCaseを使用してSpokeアプリを起動
     * ContextLinker (Utils) への直接依存を排除しました。
     */
    fun launchSpokeApp(context: Context, app: SpokeApp) {
        // UseCaseを実行 (invoke operatorにより関数のように呼べる)
        launchSpokeAppUseCase(
            context = context,
            app = app,
            projectId = _selectedProjectId.value,
            currentPath = _currentPath.value
        )
    }
}