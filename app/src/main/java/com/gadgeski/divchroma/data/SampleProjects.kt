package com.gadgeski.divchroma.data

/**
 * ProjectItem
 * サイドバーに表示するプロジェクト情報のモデル
 */
data class ProjectItem(
    val id: String,
    val name: String,
    val initial: String,
    val description: String = ""
)

/**
 * SampleProjects
 * 開発用・初期表示用のダミーデータ定義
 */
object SampleProjects {
    val projects = listOf(
        ProjectItem("proj1", "DivChroma", "D", "Project Context Orchestrator"),
        ProjectItem("proj2", "BugCodex", "B", "Cyberpunk Bug Tracker"),
        ProjectItem("proj3", "DailySync", "S", "Daily Report & Life Log"),
        ProjectItem("proj4", "Abbozzo", "A", "Visual Inspiration Scrap"),
        // Vetro -> VetroCodex に変更し、BugCodexとの統一感を強化
        ProjectItem("proj5", "VetroCodex", "V", "Time Tracking & Foldable Clock")
    )
}