package com.gadgeski.divchroma.ui.screen

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gadgeski.divchroma.data.FileItem
import com.gadgeski.divchroma.data.FileRepository
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

    // ファイルリスト
    private val _files = MutableStateFlow<List<FileItem>>(emptyList())
    val files: StateFlow<List<FileItem>> = _files.asStateFlow()

    // 検索クエリ
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        // 初期化時にルートディレクトリをロード
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

    /**
     * ディレクトリへ移動
     */
    fun navigateTo(path: String) {
        _currentPath.value = path
        loadFiles(path)
    }

    /**
     * 上の階層へ戻る
     */
    fun navigateUp() {
        val currentFile = java.io.File(_currentPath.value)
        val parent = currentFile.parentFile

        // ルートより上には行かせない等の制御も可能ですが、
        // 一旦は親が存在すれば移動します
        if (parent != null && parent.canRead()) {
            navigateTo(parent.absolutePath)
        }
    }

    /**
     * 検索クエリの更新
     */
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        // TODO: 実際のフィルタリングロジックはここで実装するか、
        // Flowのcombineを使って _files と _searchQuery を合成して UI に渡すのがスマートです。
        // 今回はまずクエリ保持のみ実装します。
    }

    /**
     * エコシステム連携: BugCodexを起動
     * "Context Injection" の第一歩です。
     */
    fun launchBugCodex(context: Context) {
        try {
            val intent = context.packageManager.getLaunchIntentForPackage("com.gadgeski.bugcodex")
            if (intent != null) {
                // 文脈を注入
                intent.putExtra("EXTRA_PROJECT_PATH", _currentPath.value)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } else {
                // アプリが見つからない場合の処理 (ストアへ誘導など)
                // 今回は簡易的にログ出力のみ
                println("BugCodex not installed")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}