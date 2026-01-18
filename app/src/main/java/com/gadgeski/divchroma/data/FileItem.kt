package com.gadgeski.divchroma.data

import java.io.File

/**
 * FileItem
 * ローカルファイルシステム上の単一ファイル/ディレクトリを表すデータモデル。
 * UI層で扱いやすいよう、プリミティブな情報のみを保持します。
 */
data class FileItem(
    val file: File,
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val lastModified: Long,
    val size: Long,
    val extension: String
)

/**
 * FileNode
 * UI (FileTree) 用の階層構造を持つモデル。
 * FileItemをラップし、子要素や展開状態を持ちます。
 */
data class FileNode(
    val id: String, // PathをIDとして使用
    val name: String,
    val isDirectory: Boolean,
    val children: List<FileNode> = emptyList(),
    val depth: Int = 0,
    val isExpanded: Boolean = false
)