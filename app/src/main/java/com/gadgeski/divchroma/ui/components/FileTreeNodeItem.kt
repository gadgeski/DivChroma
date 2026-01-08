package com.gadgeski.divchroma.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gadgeski.divchroma.data.FileNode
import com.gadgeski.divchroma.data.SampleFileTree
import com.gadgeski.divchroma.ui.theme.CodeFileColor
import com.gadgeski.divchroma.ui.theme.DivChromaTheme
import com.gadgeski.divchroma.ui.theme.FileColor
import com.gadgeski.divchroma.ui.theme.FileTreeFolderStyle
import com.gadgeski.divchroma.ui.theme.FileTreeItemStyle
import com.gadgeski.divchroma.ui.theme.FolderColor
import com.gadgeski.divchroma.ui.theme.NeonEmerald
import com.gadgeski.divchroma.ui.theme.TextPrimary

/**
 * FileTreeNodeItem - Represents a single node in the file tree
 * Features:
 * - Glassmorphism card style
 * - Animated expand/collapse
 * - Folder/file icons with appropriate colors
 * - Indentation based on depth
 */
@Composable
fun FileTreeNodeItem(
    node: FileNode,
    modifier: Modifier = Modifier,
    onNodeClick: (FileNode) -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(false) }
    
    // Arrow rotation animation
    val arrowRotation by animateFloatAsState(
        targetValue = if (isExpanded) 0f else -90f,
        animationSpec = tween(durationMillis = 200),
        label = "arrow_rotation"
    )
    
    Column(modifier = modifier.fillMaxWidth()) {
        // Main node item
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = (node.depth * 16).dp,
                    end = 4.dp,
                    top = 2.dp,
                    bottom = 2.dp
                ),
            isActive = isExpanded && node.isDirectory,
            cornerRadius = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (node.isDirectory) {
                            isExpanded = !isExpanded
                        }
                        onNodeClick(node)
                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Expand/Collapse arrow for directories
                if (node.isDirectory) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        modifier = Modifier
                            .size(18.dp)
                            .rotate(arrowRotation),
                        tint = NeonEmerald.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                } else {
                    Spacer(modifier = Modifier.width(22.dp))
                }
                
                // Node icon
                Icon(
                    imageVector = when {
                        node.isDirectory && isExpanded -> Icons.Default.FolderOpen
                        node.isDirectory -> Icons.Default.Folder
                        else -> Icons.Default.Description
                    },
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = getFileIconColor(node)
                )
                
                Spacer(modifier = Modifier.width(10.dp))
                
                // Node name - Conditional styling
                Text(
                    text = node.name,
                    style = if (node.isDirectory) FileTreeFolderStyle else FileTreeItemStyle,
                    color = TextPrimary
                )
            }
        }
        
        // Children (animated)
        AnimatedVisibility(
            visible = isExpanded && node.isDirectory,
            enter = expandVertically(animationSpec = tween(200)) + fadeIn(),
            exit = shrinkVertically(animationSpec = tween(200)) + fadeOut()
        ) {
            Column {
                node.children.forEach { child ->
                    FileTreeNodeItem(
                        node = child.copy(depth = node.depth + 1),
                        onNodeClick = onNodeClick
                    )
                }
            }
        }
    }
}

/**
 * Determines icon color based on file type
 */
@Composable
private fun getFileIconColor(node: FileNode): androidx.compose.ui.graphics.Color {
    return when {
        node.isDirectory -> FolderColor
        node.name.endsWith(".kt") || node.name.endsWith(".java") -> CodeFileColor
        node.name.endsWith(".xml") -> FileColor
        node.name.endsWith(".md") -> NeonEmerald.copy(alpha = 0.8f)
        else -> FileColor
    }
}

/**
 * FileTree - Displays the entire file tree
 */
@Composable
fun FileTree(
    nodes: List<FileNode>,
    modifier: Modifier = Modifier,
    onNodeClick: (FileNode) -> Unit = {}
) {
    Column(modifier = modifier) {
        nodes.forEach { node ->
            FileTreeNodeItem(
                node = node,
                onNodeClick = onNodeClick
            )
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun FileTreeNodeItemPreview() {
    DivChromaTheme {
        Column(modifier = Modifier.padding(8.dp)) {
            FileTree(
                nodes = SampleFileTree.sampleProject.take(2)
            )
        }
    }
}
