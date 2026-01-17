package com.gadgeski.divchroma.ui.theme

import androidx.compose.ui.graphics.Color

// ═══════════════════════════════════════════════════════════════
// DivChroma - Logic Green Cyberpunk Color Palette
// (Refined for Material 3 Implementation)
// ═══════════════════════════════════════════════════════════════

// Primary Accent - Neon Emerald
val NeonEmerald = Color(0xFF00FF9D)      // Main Accent / Primary
val MalachiteGreen = Color(0xFF10D164)   // Secondary Accent
val NeonEmeraldDim = Color(0xFF00CC7E)   // Dimmed Version

// Background Colors - Circuit Wall
val CircuitBackground = Color(0xFF050505) // Main Background (Nearly Black)
val CircuitSurface = Color(0xFF0A0A0A)    // Surface / Card Background

// Glass Effect Colors (Retained for GlassCard.kt)
val GlassSurface = Color(0xFF001100).copy(alpha = 0.5f)
val GlassBorder = Color(0xFF00FF9D).copy(alpha = 0.3f)
val GlassHighlight = Color(0x1A00FF9D)

// Text Colors
val TextPrimary = Color(0xFFE0E0E0)
val TextSecondary = Color(0xFFB0B0B0)
val TextMuted = Color(0xFF707070)

// Functional Colors
val ErrorRed = Color(0xFFFF5252)
val BorderGray = Color(0xFF2A2A2A) // Inactive State

// -----------------------------------------------------------
// Material 3 Color Scheme Mappings (For System Components)
// -----------------------------------------------------------
// UIコンポーネントが自動的にこれらの色を使うように紐付けます

val PrimaryColor = NeonEmerald
val OnPrimaryColor = CircuitBackground // 緑の上の文字は黒
val SecondaryColor = MalachiteGreen
val OnSecondaryColor = CircuitBackground

val BackgroundColor = CircuitBackground
val OnBackgroundColor = TextPrimary
val SurfaceColor = CircuitSurface
val OnSurfaceColor = TextPrimary

val ErrorColor = ErrorRed
val OnErrorColor = CircuitBackground