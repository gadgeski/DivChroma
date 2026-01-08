package com.gadgeski.divchroma.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gadgeski.divchroma.data.FileItem
import com.gadgeski.divchroma.data.FileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * MainViewModel - Manages the state for the MainScreen
 */
class MainViewModel : ViewModel() {
    private val repository = FileRepository()

    private val _currentPath = MutableStateFlow("")
    val currentPath: StateFlow<String> = _currentPath.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _files = MutableStateFlow(emptyList<FileItem>())
    // filteredFiles depends on _files and _searchQuery
    val files: StateFlow<List<FileItem>> = kotlinx.coroutines.flow.combine(_files, _searchQuery) { files, query ->
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
        val file = java.io.File(path)
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
        // If current path is empty, we consider it root (though technically Environment.getExternalStorageDirectory().path is actual root)
        // But logic in repository uses "" as default.
        // Let's refine: If current path is empty or same as root, return false.
        
        val rootPath = android.os.Environment.getExternalStorageDirectory().path
        if (current.isEmpty() || current == rootPath) {
            return false
        }

        val parent = java.io.File(current).parent ?: return false
        
        // Safety check: don't go above root if we want to restrict
        // For now, allow going up to system root if user wants, but typically we stop at /storage/emulated/0
        if (rootPath.startsWith(parent) && parent.length < rootPath.length) {
             // If parent is shorter than root path, we are exiting our "sandbox". 
             // Depending on requirement, we might stop here.
             // Let's stop at rootPath for "app home".
             // Actually, simply:
             loadFiles("") // Reset to default root
             return true
        }

        loadFiles(parent)
        return true
    }
}
