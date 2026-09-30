package com.adventure.pocketbillboard.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.adventure.pocketbillboard.data.Billboard
import com.adventure.pocketbillboard.ui.components.performHapticFeedback
import com.adventure.pocketbillboard.ui.viewmodel.BillboardViewModel

@Composable
fun MainScreen(
    viewModel: BillboardViewModel,
    onLaunchBillboard: (String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val billboards by viewModel.billboards.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = {
                        performHapticFeedback(context)
                        selectedTab = 0
                    },
                    icon = { Icon(Icons.Default.AddCircle, contentDescription = "Create") },
                    label = { Text("Create") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = {
                        performHapticFeedback(context)
                        selectedTab = 1
                    },
                    icon = { Icon(Icons.Default.Bookmark, contentDescription = "Boards") },
                    label = { Text("Boards") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = {
                        performHapticFeedback(context)
                        selectedTab = 2
                    },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> CreateScreen(
                    onSaveAndShow = { newBillboard ->
                        viewModel.saveBillboard(newBillboard)
                        onLaunchBillboard(newBillboard.id)
                    },
                    onSaveOnly = { newBillboard ->
                        viewModel.saveBillboard(newBillboard)
                    }
                )
                1 -> BoardsScreen(
                    billboards = billboards,
                    onBillboardClick = { id -> onLaunchBillboard(id) },
                    onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
                    onDelete = { id -> viewModel.deleteBillboard(id) }
                )
                2 -> SettingsScreen()
            }
        }
    }
}
