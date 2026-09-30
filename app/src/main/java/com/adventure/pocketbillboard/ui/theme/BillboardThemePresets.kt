package com.adventure.pocketbillboard.ui.theme

import androidx.compose.ui.graphics.Color

data class BillboardThemePreset(
    val id: String,
    val name: String,
    val backgroundColor: Color,
    val textColor: Color,
    val cardColor: Color
)

object BillboardThemes {
    val presets = listOf(
        BillboardThemePreset(
            id = "night",
            name = "Night",
            backgroundColor = DeepInkBg,
            textColor = ElectricAccent,
            cardColor = SurfaceCardDark
        ),
        BillboardThemePreset(
            id = "classic",
            name = "Classic",
            backgroundColor = Color.White,
            textColor = Color.Black,
            cardColor = Color.LightGray
        ),
        BillboardThemePreset(
            id = "neon",
            name = "Neon",
            backgroundColor = Color.Black,
            textColor = Color(0xFF00FFFF),
            cardColor = Color(0xFF111111)
        ),
        BillboardThemePreset(
            id = "ticket",
            name = "Ticket",
            backgroundColor = Color(0xFFFFF3B0),
            textColor = Color(0xFF333333),
            cardColor = Color(0xFFFFEE88)
        ),
        BillboardThemePreset(
            id = "departure",
            name = "Departure",
            backgroundColor = Color(0xFF121212),
            textColor = Color(0xFFFF8800),
            cardColor = Color(0xFF1E1E1E)
        ),
        BillboardThemePreset(
            id = "highlight",
            name = "Highlight",
            backgroundColor = Color(0xFF3A86EF),
            textColor = Color.White,
            cardColor = Color(0xFF2667CE)
        )
    )

    fun getPreset(id: String): BillboardThemePreset {
        return presets.find { it.id == id } ?: presets[0]
    }
}
