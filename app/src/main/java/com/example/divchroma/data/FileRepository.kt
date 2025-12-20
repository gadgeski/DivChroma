package com.example.divchroma.data

import android.os.Environment
import java.io.File

/**
 * FileRepository - Handles file system operations
 */
class FileRepository {

    /**
     * Get list of files in the specified path
     * Defaults to External Storage Directory if path is empty
     */
    fun getFiles(path: String = ""): List<FileItem> {
        val targetPath = path.ifEmpty {
            Environment.getExternalStorageDirectory().path
        }

        val directory = File(targetPath)
        
        if (!directory.exists() || !directory.isDirectory) {
            return emptyList()
        }

        return directory.listFiles()?.map { file ->
            FileItem(
                name = file.name,
                path = file.path,
                isDirectory = file.isDirectory,
                size = file.length(),
                lastModified = file.lastModified()
            )
        }?.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() })) 
        ?: emptyList()
    }
}
