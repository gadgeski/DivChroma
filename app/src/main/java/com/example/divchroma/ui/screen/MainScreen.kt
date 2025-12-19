package com.example.divchroma.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.divchroma.data.SampleFileTree
import com.example.divchroma.data.SampleProjects
import com.example.divchroma.ui.components.CircuitBackground
import com.example.divchroma.ui.components.FileTree
import com.example.divchroma.ui.components.ProjectSidebar
import com.example.divchroma.ui.theme.AppTitleStyle
import com.example.divchroma.ui.theme.DivChromaTheme

/**
 * MainScreen - Primary UI layout for DivChroma
 * Layout:
 * - Left: ProjectSidebar (narrow vertical strip)
 * - Right: FileTree (main content area)
 */
@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    var selectedProjectId by remember { mutableStateOf("proj1") }
    
    CircuitBackground(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // App Title Header - Neon Glow Effect
            Text(
                text = "DivChroma",
                style = AppTitleStyle.copy(
                    shadow = Shadow(
                        color = Color(0xFF00FFCC).copy(alpha = 0.6f),
                        blurRadius = 12f
                    )
                ),
                color = Color(0xFF00FFCC), // Neon Cyan
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 16.dp,
                    bottom = 8.dp
                )
            )
            
            Row(modifier = Modifier.fillMaxSize()) {
                // Left Sidebar - Project Tabs
                ProjectSidebar(
                    projects = SampleProjects.projects,
                    selectedProjectId = selectedProjectId,
                    modifier = Modifier.fillMaxHeight(),
                    onProjectSelected = { project ->
                        selectedProjectId = project.id
                    }
                )
                
                // Main Content Area - File Tree
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(
                            top = 8.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        )
                        .verticalScroll(rememberScrollState())
                ) {
                    FileTree(
                        nodes = SampleFileTree.sampleProject,
                        modifier = Modifier.padding(start = 8.dp),
                        onNodeClick = { node ->
                            // Handle node click - log or process the selected node
                            // For now, we reference node.name to acknowledge the click
                            println("DivChroma: Selected ${node.name}")
                        }
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF050505,
    widthDp = 400,
    heightDp = 800
)
@Composable
private fun MainScreenPreview() {
    DivChromaTheme {
        MainScreen()
    }
}
