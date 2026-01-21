package com.gadgeski.divchroma.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gadgeski.divchroma.data.FileNode
import com.gadgeski.divchroma.ui.theme.CodeFileColor
import com.gadgeski.divchroma.ui.theme.FileTreeItemStyle
import com.gadgeski.divchroma.ui.theme.FolderColor
import com.gadgeski.divchroma.ui.theme.TextSecondary

@Composable
fun FileTree(
    nodes: List<FileNode>,
    modifier: Modifier = Modifier,
    onNodeClick: (FileNode) -> Unit,
    // Add: 長押しイベントを追加
    onNodeLongClick: (FileNode) -> Unit = {}
) {
    if (nodes.isEmpty()) {
        EmptyStateMessage(modifier)
    } else {
        LazyColumn(
            modifier = modifier.fillMaxWidth()
        ) {
            items(nodes) { node ->
                FileTreeNodeItem(
                    node = node,
                    onClick = { onNodeClick(node) },
                    onLongClick = { onNodeLongClick(node) }
                )
                // ハッカーライクな区切り線（極細）
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = Color.White.copy(alpha = 0.05f)
                )
            }
        }
    }
}

@Composable
private fun EmptyStateMessage(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "NO DATA SEQUENCE",
            style = FileTreeItemStyle,
            color = TextSecondary.copy(alpha = 0.5f)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileTreeNodeItem(
    node: FileNode,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            // combinedClickable を使用して長押しに対応
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        val icon = if (node.isDirectory) Icons.Default.Folder else Icons.Default.Description
        val tint = if (node.isDirectory) FolderColor else CodeFileColor

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Text
        Text(
            text = node.name,
            style = FileTreeItemStyle,
            color = TextSecondary
        )
    }
}