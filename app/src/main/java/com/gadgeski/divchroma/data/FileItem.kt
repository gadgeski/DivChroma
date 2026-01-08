package com.example.divchroma.data

/**
 * FileItem - Data class representing a file in the system
 */
data class FileItem(
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val size: Long,
    val lastModified: Long
)
