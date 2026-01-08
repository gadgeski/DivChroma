package com.gadgeski.divchroma.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gadgeski.divchroma.data.FileNode
import com.gadgeski.divchroma.data.SampleProjects
import com.gadgeski.divchroma.ui.components.CircuitBackground
import com.gadgeski.divchroma.ui.components.FileTree
import com.gadgeski.divchroma.ui.components.GlassCard
import com.gadgeski.divchroma.ui.components.ProjectSidebar
import com.gadgeski.divchroma.ui.theme.DeepMetallic
import com.gadgeski.divchroma.ui.theme.DivChromaTheme
import com.gadgeski.divchroma.ui.theme.GlassBorder
import com.gadgeski.divchroma.ui.theme.NeonEmerald
import com.gadgeski.divchroma.ui.theme.SectionHeaderStyle
import com.gadgeski.divchroma.ui.theme.TextPrimary
import com.gadgeski.divchroma.ui.theme.TextSecondary
import com.gadgeski.divchroma.ui.theme.Typography

/**
 * MainScreen - Primary UI layout for DivChroma
 * Layout:
 * - Left: ProjectSidebar (narrow vertical strip)
 * - Right: FileTree (main content area)
 */
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    var selectedProjectId by remember { mutableStateOf("proj1") }
    val files by viewModel.files.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current
    val isSearchVisible = rememberSaveable { mutableStateOf(false) }
    val searchQuery by viewModel.searchQuery.collectAsState()
    
    // Map FileItems to FileNodes for the UI
    val fileNodes = remember(files) {
        files.map { fileItem ->
            FileNode(
                id = fileItem.path,
                name = fileItem.name,
                isDirectory = fileItem.isDirectory,
                children = emptyList(), // Recursive loading not yet implemented
                depth = 0
            )
        }
    }
    
    // Back Handling - Valid implementation
    // Navigate up if not at root
    val currentPath by viewModel.currentPath.collectAsState()
    val isRoot = currentPath.isEmpty() || currentPath == android.os.Environment.getExternalStorageDirectory().path
    
    BackHandler(enabled = !isRoot) {
        viewModel.navigateUp()
    }

    CircuitBackground(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent, // Transparent to show CircuitBackground
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* Handle Add */ },
                    containerColor = NeonEmerald,
                    contentColor = Color.Black,
                    shape = RoundedCornerShape(16.dp),
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 4.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Item"
                    )
                }
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xFF00221C), // Calm Deep Green
                    modifier = Modifier.border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                NeonEmerald.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        ),
                        shape = RectangleShape
                    )
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Folder, contentDescription = "Files") },
                        label = { Text("Files") },
                        selected = true,
                        onClick = { },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonEmerald,
                            selectedTextColor = NeonEmerald,
                            indicatorColor = NeonEmerald.copy(alpha = 0.2f),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )
                    NavigationBarItem(
                        selected = isSearchVisible.value,
                        // .value を追加
                        onClick = {
                            // 下記に.value をつけることで、IDEに「読んでるし書いてるよ！」と伝えます。
                            isSearchVisible.value = !isSearchVisible.value
                        },
                        icon = { Icon(Icons.Default.Search, null) },
                        label = { Text("Search") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonEmerald,
                            selectedTextColor = NeonEmerald,
                            indicatorColor = DeepMetallic
                        )
                    )
                     NavigationBarItem(
                        selected = false,
                        onClick = { },
                        icon = { Icon(Icons.Default.Settings, null) },
                        label = { Text("Settings") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonEmerald,
                            selectedTextColor = NeonEmerald,
                            indicatorColor = DeepMetallic
                        )
                    )
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Left Sidebar - Project Tabs
                ProjectSidebar(
                    projects = SampleProjects.projects,
                    selectedProjectId = selectedProjectId,
                    modifier = Modifier.fillMaxHeight(),
                    onProjectSelected = { project ->
                        selectedProjectId = project.id
                    }
                )
                
                // Main Content Area
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    // Dashboard Header
                    DashboardHeader()
                    
                    // Address Bar (Breadcrumbs)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        val displayPath = if (currentPath.isEmpty()) "> root" else "> ...${currentPath.takeLast(30)}"
                        Text(
                            text = displayPath,
                            color = NeonEmerald,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            fontSize = 14.sp
                        )
                    }

                    // Search Bar
                    if (isSearchVisible.value) {
                         OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.onSearchQueryChange(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            placeholder = { Text("Search files...", color = Color.Gray) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = NeonEmerald,
                                unfocusedTextColor = TextPrimary,
                                focusedContainerColor = Color.Black.copy(alpha = 0.5f),
                                unfocusedContainerColor = Color.Black.copy(alpha = 0.3f),
                                focusedBorderColor = NeonEmerald,
                                unfocusedBorderColor = GlassBorder
                            ),
                            trailingIcon = {
                                IconButton(onClick = { 
                                    viewModel.onSearchQueryChange("")
                                    isSearchVisible.value = false
                                }) {
                                    Icon(Icons.Default.Close, contentDescription = "Close Search", tint = NeonEmerald)
                                }
                            }
                        )
                    }

                    // File Tree Content
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        FileTree(
                            nodes = fileNodes,
                            modifier = Modifier.padding(bottom = 80.dp), // Check FAB overlap
                            onNodeClick = { node ->
                                if (node.isDirectory) {
                                    viewModel.navigateTo(node.id) // node.id holds path
                                } else {
                                    // Open file
                                    com.gadgeski.divchroma.utils.FileOpener.openFile(context, java.io.File(node.id))
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardHeader() {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        isActive = true, // Force active style for glow
        cornerRadius = 12.dp,
        borderWidth = 1.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "PROJECT STATUS",
                    style = SectionHeaderStyle,
                    color = NeonEmerald,
                    fontSize = 12.sp
                )
                Text(
                    text = "ACTIVE",
                    style = Typography.headlineMedium,
                    color = TextPrimary
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                 Text(
                    text = "TOTAL FILES",
                    style = SectionHeaderStyle, // Using SectionHeader for labels
                    color = TextSecondary,
                    fontSize = 10.sp
                )
                 Text(
                    text = "12",
                     style = Typography.titleLarge,
                     color = TextPrimary
                )
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
