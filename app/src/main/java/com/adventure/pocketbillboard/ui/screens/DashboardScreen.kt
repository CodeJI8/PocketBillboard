package com.adventure.pocketbillboard.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.adventure.pocketbillboard.data.Billboard
import com.adventure.pocketbillboard.ui.theme.BillboardThemes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    billboards: List<Billboard>,
    onBillboardClick: (String) -> Unit,
    onEditClick: (String?) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(FilterTab.ALL) }

    val filteredBillboards = billboards.filter { billboard ->
        val matchesSearch = billboard.title.contains(searchQuery, ignoreCase = true) ||
                billboard.text.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (selectedFilter) {
            FilterTab.ALL -> true
            FilterTab.FAVORITES -> billboard.isFavorite
            FilterTab.PRESETS -> billboard.id.startsWith("default_")
        }
        matchesSearch && matchesFilter
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Pocket Billboard",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "High-contrast distance displays",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEditClick(null) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Billboard")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search your billboards...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == FilterTab.ALL,
                    onClick = { selectedFilter = FilterTab.ALL },
                    label = { Text("All (${billboards.size})") }
                )
                FilterChip(
                    selected = selectedFilter == FilterTab.FAVORITES,
                    onClick = { selectedFilter = FilterTab.FAVORITES },
                    label = { Text("Favorites") }
                )
                FilterChip(
                    selected = selectedFilter == FilterTab.PRESETS,
                    onClick = { selectedFilter = FilterTab.PRESETS },
                    label = { Text("Templates") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredBillboards.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No billboards found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(filteredBillboards, key = { it.id }) { billboard ->
                        BillboardCard(
                            billboard = billboard,
                            onClick = { onBillboardClick(billboard.id) },
                            onEdit = { onEditClick(billboard.id) },
                            onToggleFavorite = { onToggleFavorite(billboard.id) },
                            onDelete = { onDeleteClick(billboard.id) }
                        )
                    }
                }
            }
        }
    }
}

enum class FilterTab {
    ALL, FAVORITES, PRESETS
}

@Composable
fun BillboardCard(
    billboard: Billboard,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit
) {
    val preset = BillboardThemes.getPreset(billboard.themePresetId)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = preset.backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = billboard.title,
                    style = MaterialTheme.typography.labelLarge,
                    color = preset.textColor.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold
                )

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (billboard.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = preset.textColor
                        )
                    }
                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = preset.textColor
                        )
                    }
                    if (!billboard.id.startsWith("default_")) {
                        IconButton(onClick = onDelete) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = preset.textColor.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = billboard.text,
                style = MaterialTheme.typography.headlineMedium,
                color = preset.textColor,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = preset.textColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = preset.name,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = preset.textColor
                    )
                }

                Text(
                    text = "Tap to launch full screen ➔",
                    style = MaterialTheme.typography.labelMedium,
                    color = preset.textColor.copy(alpha = 0.8f),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
