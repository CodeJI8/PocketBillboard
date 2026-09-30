package com.adventure.pocketbillboard.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.adventure.pocketbillboard.data.Billboard
import com.adventure.pocketbillboard.ui.theme.BillboardThemePreset
import com.adventure.pocketbillboard.ui.theme.BillboardThemes
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    billboardToEdit: Billboard?,
    onSave: (Billboard) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf(billboardToEdit?.title ?: "") }
    var text by remember { mutableStateOf(billboardToEdit?.text ?: "") }
    var selectedThemeId by remember { mutableStateOf(billboardToEdit?.themePresetId ?: "obsidian_neon") }
    var selectedFontStyle by remember { mutableStateOf(billboardToEdit?.fontStyle ?: "sans_bold") }

    val currentPreset = BillboardThemes.getPreset(selectedThemeId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (billboardToEdit == null) "New Billboard" else "Edit Billboard") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (text.isNotBlank()) {
                                val billboard = Billboard(
                                    id = billboardToEdit?.id ?: UUID.randomUUID().toString(),
                                    title = if (title.isBlank()) "Untitled" else title,
                                    text = text,
                                    themePresetId = selectedThemeId,
                                    fontStyle = selectedFontStyle,
                                    isFavorite = billboardToEdit?.isFavorite ?: false,
                                    createdAt = billboardToEdit?.createdAt ?: System.currentTimeMillis()
                                )
                                onSave(billboard)
                            }
                        }
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Live Preview Card
            Text(
                text = "Live Preview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = currentPreset.backgroundColor)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (text.isBlank()) "Your Text Here" else text,
                        style = MaterialTheme.typography.headlineMedium,
                        color = currentPreset.textColor,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 3
                    )
                }
            }

            // Quick Symbols Row
            Text(
                text = "Quick Symbols",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val symbols = listOf("✈️", "🚖", "📶", "⚠️", "⭐", "⬅️", "➡️", "⬆️", "⬇️", "💼", "🍕", "☕")
                symbols.forEach { symbol ->
                    OutlinedButton(
                        onClick = { text += symbol },
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(text = symbol, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            // Title Input
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Billboard Title (e.g., Uber Pickup)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            // Text Input
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Display Text (Huge, high contrast)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(16.dp)
            )

            // Theme Preset Selector
            Text(
                text = "High-Contrast Color Theme",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BillboardThemes.presets.forEach { preset ->
                    ThemePresetChip(
                        preset = preset,
                        isSelected = preset.id == selectedThemeId,
                        onClick = { selectedThemeId = preset.id }
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Save Button
            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        val billboard = Billboard(
                            id = billboardToEdit?.id ?: UUID.randomUUID().toString(),
                            title = if (title.isBlank()) "Untitled" else title,
                            text = text,
                            themePresetId = selectedThemeId,
                            fontStyle = selectedFontStyle,
                            isFavorite = billboardToEdit?.isFavorite ?: false,
                            createdAt = billboardToEdit?.createdAt ?: System.currentTimeMillis()
                        )
                        onSave(billboard)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Save & Display",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun ThemePresetChip(
    preset: BillboardThemePreset,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val borderWidth = if (isSelected) 3.dp else 1.dp

    Column(
        modifier = Modifier
            .width(90.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(borderWidth, borderColor, RoundedCornerShape(16.dp))
            .background(preset.backgroundColor)
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(preset.textColor, RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = preset.name,
            style = MaterialTheme.typography.labelSmall,
            color = preset.textColor,
            maxLines = 1
        )
    }
}
