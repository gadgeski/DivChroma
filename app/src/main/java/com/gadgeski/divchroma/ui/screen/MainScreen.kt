package com.gadgeski.divchroma.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gadgeski.divchroma.data.FileNode
import com.gadgeski.divchroma.data.SampleProjects
import com.gadgeski.divchroma.data.SpokeApp
import com.gadgeski.divchroma.ui.components.CircuitBackground
import com.gadgeski.divchroma.ui.components.FileTree
import com.gadgeski.divchroma.ui.components.GlassCard
import com.gadgeski.divchroma.ui.components.PermissionGate
import com.gadgeski.divchroma.ui.components.ProjectSidebar
import com.gadgeski.divchroma.ui.theme.DivChromaTheme
import com.gadgeski.divchroma.ui.theme.GlassBorder
import com.gadgeski.divchroma.ui.theme.NeonEmerald
import com.gadgeski.divchroma.ui.theme.SectionHeaderStyle
import com.gadgeski.divchroma.ui.theme.TextPrimary
import com.gadgeski.divchroma.ui.theme.TextSecondary
import com.gadgeski.divchroma.ui.theme.Typography
import com.gadgeski.divchroma.utils.FileOpener
import java.io.File

/**
 * MainScreen - Primary UI layout for DivChroma
 */
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    // PermissionGate Wrap
    PermissionGate {
        MainScreenContent(modifier, viewModel)
    }
}

@Composable
private fun MainScreenContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    val selectedProjectId by viewModel.selectedProjectId.collectAsState()
    val files by viewModel.files.collectAsState()
    val context = LocalContext.current
    val isSearchVisible = rememberSaveable { mutableStateOf(false) }
    val searchQuery by viewModel.searchQuery.collectAsState()
    val currentPath by viewModel.currentPath.collectAsState()

    // UI Mapping
    val fileNodes = remember(files) {
        files.map { fileItem ->
            FileNode(
                id = fileItem.path,
                name = fileItem.name,
                isDirectory = fileItem.isDirectory,
                children = emptyList(),
                depth = 0
            )
        }
    }

    val isRoot = currentPath.isEmpty() || currentPath == android.os.Environment.getExternalStorageDirectory().path

    BackHandler(enabled = !isRoot) {
        viewModel.navigateUp()
    }

    CircuitBackground(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
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
                    Icon(Icons.Default.Add, contentDescription = "Add Item")
                }
            },
            bottomBar = {
                // Bottom Bar Implementation
                NavigationBar(
                    containerColor = Color(0xFF00221C),
                    modifier = Modifier.border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(NeonEmerald.copy(alpha = 0.3f), Color.Transparent)
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
                        onClick = { isSearchVisible.value = !isSearchVisible.value },
                        icon = { Icon(Icons.Default.Search, null) },
                        label = { Text("Search") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonEmerald,
                            selectedTextColor = NeonEmerald,
                            indicatorColor = MaterialTheme.colorScheme.surfaceVariant
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
                            indicatorColor = MaterialTheme.colorScheme.surfaceVariant
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
                // Project Sidebar
                ProjectSidebar(
                    projects = SampleProjects.projects,
                    selectedProjectId = selectedProjectId,
                    modifier = Modifier.fillMaxHeight(),
                    onProjectSelected = { project ->
                        viewModel.onProjectSelected(project.id)
                    }
                )

                // Main Area
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    // Dashboard Header (Linker)
                    DashboardHeader(
                        fileCount = fileNodes.size,
                        onLaunchApp = { app ->
                            viewModel.launchSpokeApp(context, app)
                        }
                    )

                    // Address Bar
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
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp
                        )
                    }

                    // Search Input
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

                    // File List
                    // FIX: verticalScrollを削除。FileTree内のLazyColumnにスクロールを任せる
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                        // .verticalScroll(rememberScrollState()) <-- 削除しました
                    ) {
                        FileTree(
                            nodes = fileNodes,
                            modifier = Modifier.padding(bottom = 80.dp),
                            onNodeClick = { node ->
                                if (node.isDirectory) {
                                    viewModel.navigateTo(node.id)
                                } else {
                                    FileOpener.openFile(context, File(node.id))
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

// DashboardHeader, AppLaunchButton, Preview... (変更なし)
@Composable
fun DashboardHeader(
    modifier: Modifier = Modifier,
    fileCount: Int = 0,
    onLaunchApp: (SpokeApp) -> Unit = {}
) {
    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        isActive = true,
        cornerRadius = 12.dp,
        borderWidth = 1.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Top Row: Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "PROJECT STATUS", style = SectionHeaderStyle, color = NeonEmerald, fontSize = 12.sp)
                    Text(text = "ACTIVE", style = Typography.headlineMedium, color = TextPrimary)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "TOTAL FILES", style = SectionHeaderStyle, color = TextSecondary, fontSize = 10.sp)
                    Text(text = "$fileCount", style = Typography.titleLarge, color = TextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Row: Launchers (4 Apps)
            Text(text = "LINKED MODULES", style = SectionHeaderStyle, color = TextSecondary, fontSize = 10.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // BugCodex, DailySync, Abbozzo, VetroCodex
                SpokeApp.entries.forEach { app ->
                    AppLaunchButton(
                        app = app,
                        onClick = { onLaunchApp(app) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun AppLaunchButton(
    app: SpokeApp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilledTonalButton(
        onClick = onClick,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = NeonEmerald.copy(alpha = 0.1f),
            contentColor = NeonEmerald
        ),
        contentPadding = PaddingValues(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.height(48.dp) // 高さ固定で揃える
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = app.icon,
                contentDescription = app.appName,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = app.actionLabel, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun MainScreenPreview() {
    DivChromaTheme {
        // Preview
    }
}