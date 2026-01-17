package com.gadgeski.divchroma.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.gadgeski.divchroma.R

// ===========================================================================
// Hybrid Typography Strategy (Refined by PrismNexus)
// ===========================================================================
// 1. Headlines (BBH Hegarty): Impact, Brand Identity, Section Markers
// 2. Content (Monospace): ALL Data, Logs, Body text - for pure techno-utilitarian look
// ===========================================================================

/**
 * BBH Hegarty - Custom display font
 */
val BbhHegartyFamily = FontFamily(
    Font(R.font.bbhhegarty_regular, FontWeight.Normal)
)

/**
 * Monospace - System monospace font
 * Used extensively to maintain the "Hacker/Console" aesthetic
 */
val MonospaceFamily = FontFamily.Monospace

// ===========================================================================
// Custom Text Styles (Preserved)
// ===========================================================================

val SectionHeaderStyle = TextStyle(
    fontFamily = BbhHegartyFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 20.sp,
    lineHeight = 26.sp,
    letterSpacing = 1.5.sp
)

val FileTreeItemStyle = TextStyle(
    fontFamily = MonospaceFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.sp
)

val FileTreeFolderStyle = TextStyle(
    fontFamily = MonospaceFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.sp
)

// ===========================================================================
// Material 3 Typography
// ===========================================================================

val Typography = Typography(
    // Display - BBH Hegarty
    displayLarge = TextStyle(
        fontFamily = BbhHegartyFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = BbhHegartyFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = BbhHegartyFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),

    // Headline - BBH Hegarty
    headlineLarge = TextStyle(
        fontFamily = BbhHegartyFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = BbhHegartyFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = BbhHegartyFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    // Title - BBH Hegarty (for emphasis)
    titleLarge = TextStyle(
        fontFamily = BbhHegartyFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    // Medium/Small Titles are often used inside cards/lists, so Monospace fits better here
    // But keeping BBH for consistency with user's original intent if it's "Title"
    titleMedium = TextStyle(
        fontFamily = BbhHegartyFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = BbhHegartyFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // Body - Switch to Monospace for Tech Feel
    bodyLarge = TextStyle(
        fontFamily = MonospaceFamily, // Changed from Default
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = MonospaceFamily, // Changed from Default
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = MonospaceFamily, // Changed from Default
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),

    // Label - Switch to Monospace for Tech Feel
    labelLarge = TextStyle(
        fontFamily = MonospaceFamily, // Changed from Default
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = MonospaceFamily, // Changed from Default
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = MonospaceFamily, // Changed from Default
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)