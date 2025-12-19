package com.example.divchroma.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.divchroma.data.ProjectTab
import com.example.divchroma.data.SampleProjects
import com.example.divchroma.ui.theme.ActiveGlow
import com.example.divchroma.ui.theme.CircuitSurface
import com.example.divchroma.ui.theme.DarkMetallicGreen
import com.example.divchroma.ui.theme.DivChromaTheme
import com.example.divchroma.ui.theme.InactiveState
import com.example.divchroma.ui.theme.NeonEmerald
import com.example.divchroma.ui.theme.SectionHeaderStyle
import com.example.divchroma.ui.theme.TabLabelStyle
import com.example.divchroma.ui.theme.TextMuted
import com.example.divchroma.ui.theme.TextPrimary

/**
 * ProjectSidebar - Narrow vertical sidebar for project tabs
 * Features:
 * - Project icons displayed vertically
 * - Selected item has green LED indicator
 * - Physical "switch" appearance
 */
@Composable
fun ProjectSidebar(
    projects: List<ProjectTab>,
    selectedProjectId: String,
    modifier: Modifier = Modifier,
    onProjectSelected: (ProjectTab) -> Unit = {}
) {
    Column(
        modifier = modifier
            .width(64.dp)
            .fillMaxHeight()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        CircuitSurface,
                        Color(0xFF080808)
                    )
                )
            )
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        projects.forEach { project ->
            ProjectTabItem(
                project = project,
                isSelected = project.id == selectedProjectId,
                onClick = { onProjectSelected(project) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

/**
 * ProjectTabItem - Individual project icon with LED indicator
 */
@Composable
private fun ProjectTabItem(
    project: ProjectTab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
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
        // LED Indicator
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) ActiveGlow else Color(0xFF1A1A1A)
                )
                .then(
                    if (isSelected) {
                        Modifier.shadow(
                            elevation = 4.dp,
                            shape = CircleShape,
                            ambientColor = ActiveGlow,
                            spotColor = ActiveGlow
                        )
                    } else Modifier
                )
        )
        
        Spacer(modifier = Modifier.height(6.dp))
        
        // Project icon button
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(backgroundColor)
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = project.iconChar.toString(),
                style = SectionHeaderStyle,
                color = textColor,
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Project name (truncated)
        Text(
            text = project.name.take(6),
            style = TabLabelStyle,
            color = if (isSelected) TextPrimary else TextMuted,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun ProjectSidebarPreview() {
    DivChromaTheme {
        ProjectSidebar(
            projects = SampleProjects.projects,
            selectedProjectId = "proj1"
        )
    }
}
