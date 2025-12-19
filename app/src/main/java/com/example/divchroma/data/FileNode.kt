package com.example.divchroma.data

/**
 * DivChroma File Tree Data Model
 * Represents a node in the file tree hierarchy
 */
data class FileNode(
    val id: String,
    val name: String,
    val isDirectory: Boolean,
    val children: List<FileNode> = emptyList(),
    val depth: Int = 0
)

/**
 * Sample file tree for demonstration
 */
object SampleFileTree {
    
    val sampleProject = listOf(
        FileNode(
            id = "1",
            name = "app",
            isDirectory = true,
            children = listOf(
                FileNode(
                    id = "1-1",
                    name = "src",
                    isDirectory = true,
                    depth = 1,
                    children = listOf(
                        FileNode(
                            id = "1-1-1",
                            name = "main",
                            isDirectory = true,
                            depth = 2,
                            children = listOf(
                                FileNode(id = "1-1-1-1", name = "java", isDirectory = true, depth = 3),
                                FileNode(id = "1-1-1-2", name = "res", isDirectory = true, depth = 3),
                                FileNode(id = "1-1-1-3", name = "AndroidManifest.xml", isDirectory = false, depth = 3)
                            )
                        ),
                        FileNode(id = "1-1-2", name = "test", isDirectory = true, depth = 2)
                    )
                ),
                FileNode(id = "1-2", name = "build.gradle.kts", isDirectory = false, depth = 1)
            )
        ),
        FileNode(
            id = "2",
            name = "gradle",
            isDirectory = true,
            children = listOf(
                FileNode(id = "2-1", name = "wrapper", isDirectory = true, depth = 1),
                FileNode(id = "2-2", name = "libs.versions.toml", isDirectory = false, depth = 1)
            )
        ),
        FileNode(id = "3", name = "build.gradle.kts", isDirectory = false),
        FileNode(id = "4", name = "settings.gradle.kts", isDirectory = false),
        FileNode(id = "5", name = "README.md", isDirectory = false)
    )
}

/**
 * Project tab item for sidebar
 */
data class ProjectTab(
    val id: String,
    val name: String,
    val iconChar: Char // Simple representation with a character
)

object SampleProjects {
    val projects = listOf(
        ProjectTab("proj1", "DivChroma", 'D'),
        ProjectTab("proj2", "Abbozzo", 'A'),
        ProjectTab("proj3", "Vetro", 'V')
    )
}
