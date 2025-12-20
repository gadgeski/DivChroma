package com.example.divchroma.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.divchroma.data.FileItem
import com.example.divchroma.data.FileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * MainViewModel - Manages the state for the MainScreen
 */
class MainViewModel : ViewModel() {
    private val repository = FileRepository()

    private val _files = MutableStateFlow<List<FileItem>>(emptyList())
    val files: StateFlow<List<FileItem>> = _files.asStateFlow()

    init {
        loadFiles()
    }

    /**
     * Load files from the default external storage directory
     */
    fun loadFiles(path: String = "") {
        viewModelScope.launch {
            _files.value = repository.getFiles(path)
        }
    }
}
