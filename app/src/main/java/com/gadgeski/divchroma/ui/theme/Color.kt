package com.gadgeski.divchroma.ui.theme

import androidx.compose.ui.graphics.Color

// ═══════════════════════════════════════════════════════════════
// DivChroma - Logic Green Cyberpunk Color Palette
// (Refined for Material 3 Implementation)
// ═══════════════════════════════════════════════════════════════

// Primary Accent - Neon Emerald
val NeonEmerald = Color(0xFF00FF9D)
// Main Accent / Primary
val MalachiteGreen = Color(0xFF10D164)
// Secondary Accent
val NeonEmeraldDim = Color(0xFF00CC7E)
// Dimmed Version

// Secondary Accent - Dark Metallic Greens
// ★Theme.ktが必要としていた定義を追加
val DarkMetallicGreen = Color(0xFF0A2A1B)
val DeepMetallic = Color(0xFF0D1F17)
val MetallicHighlight = Color(0xFF1A4A35)

// Background Colors - Circuit Wall
val CircuitBackground = Color(0xFF050505)
// Main Background (Nearly Black)
val CircuitSurface = Color(0xFF0A0A0A)
// Surface / Card Background

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

// Inactive State
// Theme.ktで使用されているため定義
val InactiveState = Color(0xFF2A2A2A)

// File Type Colors
// FileTreeで使用される可能性があります
val FolderColor = NeonEmerald
val FileColor = Color(0xFF80CBC4)
val CodeFileColor = Color(0xFF64FFDA)

// -----------------------------------------------------------
// Cleaned Up: Unused mappings removed
// -----------------------------------------------------------
// 以前ここにあった PrimaryColor などの未使用変数は、
// Theme.kt が直接 NeonEmerald 等を参照しているため削除しました。
// これで "Property is never used" 警告は解消されます。