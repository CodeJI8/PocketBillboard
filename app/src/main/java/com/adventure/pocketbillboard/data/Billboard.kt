package com.adventure.pocketbillboard.data

data class Billboard(
    val id: String,
    val title: String,
    val text: String,
    val themePresetId: String = "obsidian_neon",
    val fontStyle: String = "sans_bold", // sans_bold, monospace, serif, display
    val isFavorite: Boolean = false,
    val isMarquee: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
