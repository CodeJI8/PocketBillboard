package com.adventure.pocketbillboard.ui.screens

import android.app.Activity
import android.view.WindowManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adventure.pocketbillboard.data.Billboard
import com.adventure.pocketbillboard.ui.theme.BillboardThemes

@Composable
fun BillboardScreen(
    billboard: Billboard,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var showControls by remember { mutableStateOf(true) }
    var isMirrored by remember { mutableStateOf(false) }
    var currentThemeId by remember { mutableStateOf(billboard.themePresetId) }

    val preset = BillboardThemes.getPreset(currentThemeId)

    // Keep screen fully awake while billboard is displayed
    DisposableEffect(Unit) {
        val window = (context as? Activity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(preset.backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                showControls = !showControls
            },
        contentAlignment = Alignment.Center
    ) {
        // Main Huge Text
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            val textToDisplay = billboard.text
            val fontSize = if (textToDisplay.length < 10) 72.sp else if (textToDisplay.length < 30) 48.sp else 32.sp

            Text(
                text = textToDisplay,
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(scaleX = if (isMirrored) -1f else 1f, scaleY = 1f),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = fontSize,
                    fontWeight = FontWeight.Black,
                    lineHeight = fontSize * 1.2f
                ),
                color = preset.textColor,
                textAlign = TextAlign.Center
            )
        }

        // Top & Bottom Controls Overlay
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Top Bar Controls
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = preset.textColor.copy(alpha = 0.2f),
                            contentColor = preset.textColor
                        )
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }

                    Text(
                        text = billboard.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = preset.textColor.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = {
                            val currentIndex = BillboardThemes.presets.indexOfFirst { it.id == currentThemeId }
                            val nextIndex = (currentIndex + 1) % BillboardThemes.presets.size
                            currentThemeId = BillboardThemes.presets[nextIndex].id
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = preset.textColor.copy(alpha = 0.2f),
                            contentColor = preset.textColor
                        )
                    ) {
                        Icon(Icons.Default.Palette, contentDescription = "Switch Theme")
                    }
                }

                // Bottom Bar Controls
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.clickable { isMirrored = !isMirrored },
                        shape = RoundedCornerShape(20.dp),
                        color = preset.textColor.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Flip,
                                contentDescription = null,
                                tint = preset.textColor
                            )
                            Text(
                                text = if (isMirrored) "Mirrored (On)" else "Mirror Mode",
                                color = preset.textColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
