package com.gadgeski.divchroma.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gadgeski.divchroma.data.ProjectItem
import com.gadgeski.divchroma.data.SampleProjects
import com.gadgeski.divchroma.ui.theme.DarkMetallicGreen
import com.gadgeski.divchroma.ui.theme.DivChromaTheme
import com.gadgeski.divchroma.ui.theme.InactiveState
import com.gadgeski.divchroma.ui.theme.NeonEmerald
import com.gadgeski.divchroma.ui.theme.SectionHeaderStyle
import com.gadgeski.divchroma.ui.theme.TextMuted

/**
 * ProjectSidebar - Narrow vertical sidebar for project tabs
 * Features:
 * - Project icons displayed vertically
 * - Selected item has green LED indicator
 * - Physical "switch" appearance with animation
 * - Refactored to use ProjectItem and current Color definitions
 */
@Composable
fun ProjectSidebar(
    projects: List<ProjectItem>,
    selectedProjectId: String,
    onProjectSelected: (ProjectItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(72.dp) // 幅を少し広げてタップしやすく
            .fillMaxHeight()
            .padding(start = 12.dp, top = 24.dp, bottom = 24.dp) // Floating Capsule Style
            .clip(RoundedCornerShape(24.dp))
            .background(Color.Black.copy(alpha = 0.4f)) // Semi-transparent dark
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(NeonEmerald.copy(alpha = 0.5f), Color.Transparent)
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Scroll state for many projects
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            projects.forEach { project ->
                ProjectTabItem(
                    project = project,
                    isSelected = project.id == selectedProjectId,
                    onClick = { onProjectSelected(project) }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

/**
 * ProjectTabItem - Individual project icon with LED indicator
 */
@Composable
private fun ProjectTabItem(
    project: ProjectItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // アニメーション: 選択時にじわっと色が変化する
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) DarkMetallicGreen else InactiveState,
        animationSpec = tween(200),
        label = "bg_color"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) NeonEmerald.copy(alpha = 0.6f) else Color(0xFF2A2A2A),
        animationSpec = tween(200),
        label = "border_color"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) NeonEmerald else TextMuted,
        animationSpec = tween(200),
        label = "text_color"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Project icon button - active state is the box itself
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(backgroundColor)
                .border(
                    width = if (isSelected) 1.5.dp else 0.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .then(
                    if (isSelected) {
                        Modifier.shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(12.dp),
                            ambientColor = NeonEmerald, // ActiveGlow -> NeonEmerald
                            spotColor = NeonEmerald     // ActiveGlow -> NeonEmerald
                        )
                    } else Modifier
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = project.initial, // iconChar -> initial
                style = SectionHeaderStyle,
                color = textColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun ProjectSidebarPreview() {
    DivChromaTheme {
        ProjectSidebar(
            projects = SampleProjects.projects,
            selectedProjectId = "proj1",
            onProjectSelected = {}
        )
    }
}