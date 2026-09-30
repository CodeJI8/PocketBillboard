package com.adventure.pocketbillboard.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adventure.pocketbillboard.data.Billboard
import com.adventure.pocketbillboard.ui.components.PrimaryButton
import com.adventure.pocketbillboard.ui.components.StyleChip
import com.adventure.pocketbillboard.ui.theme.BillboardThemes
import java.util.UUID

@Composable
fun CreateScreen(
    onSaveAndShow: (Billboard) -> Unit,
    onSaveOnly: (Billboard) -> Unit
) {
    var textInput by remember { mutableStateOf("") }
    var selectedStyleId by remember { mutableStateOf("night") }

    val currentPreset = BillboardThemes.getPreset(selectedStyleId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Title
        Text(
            text = "Create Billboard",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        // Hero Input Area
        OutlinedTextField(
            value = textInput,
            onValueChange = { textInput = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
            placeholder = { Text("What do you want to show? (e.g., ALEX - UBER)") },
            shape = RoundedCornerShape(24.dp),
            maxLines = 4
        )

        // Quick-Start Template Chips
        Text(
            text = "Quick Templates",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val templates = listOf(
                "Pickup" to "ALEX (BAGS 2)",
                "Address" to "123 MAIN ST, APT 4B",
                "Wi-Fi" to "WIFI: Guest\nPASS: coffee123",
                "Gate" to "GATE B12 - FLIGHT 402"
            )
            templates.forEach { (label, sampleText) ->
                OutlinedButton(
                    onClick = { textInput = sampleText },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(label)
                }
            }
        }

        // Live Preview Card
        Text(
            text = "Live Preview",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    text = if (textInput.isBlank()) "Preview text..." else textInput,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = if (textInput.length > 20) 24.sp else 36.sp
                    ),
                    color = currentPreset.textColor,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        // Style Chips Row
        Text(
            text = "Choose Style",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BillboardThemes.presets.forEach { preset ->
                StyleChip(
                    label = preset.name,
                    isSelected = preset.id == selectedStyleId,
                    onClick = { selectedStyleId = preset.id }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Big "Show Big" Button
        PrimaryButton(
            text = "Show Big",
            onClick = {
                val finalTxt = if (textInput.isBlank()) "POCKET BILLBOARD" else textInput
                val billboard = Billboard(
                    id = UUID.randomUUID().toString(),
                    title = finalTxt.take(20),
                    text = finalTxt,
                    themePresetId = selectedStyleId,
                    fontStyle = "sans_bold",
                    isFavorite = false,
                    createdAt = System.currentTimeMillis()
                )
                onSaveAndShow(billboard)
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
