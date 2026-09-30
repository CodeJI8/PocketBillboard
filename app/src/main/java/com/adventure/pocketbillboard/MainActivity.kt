package com.adventure.pocketbillboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.adventure.pocketbillboard.ui.screens.BillboardScreen
import com.adventure.pocketbillboard.ui.screens.MainScreen
import com.adventure.pocketbillboard.ui.theme.PocketBillboardTheme
import com.adventure.pocketbillboard.ui.viewmodel.BillboardViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: BillboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Handle ACTION_SEND intent if text was shared from another app
        val sharedText = if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            intent.getStringExtra(Intent.EXTRA_TEXT)
        } else {
            null
        }

        setContent {
            PocketBillboardTheme {
                val navController = rememberNavController()
                val billboards by viewModel.billboards.collectAsState()

                NavHost(
                    navController = navController,
                    startDestination = "main"
                ) {
                    composable("main") {
                        MainScreen(
                            viewModel = viewModel,
                            onLaunchBillboard = { id ->
                                navController.navigate("billboard/$id")
                            }
                        )
                    }

                    composable(
                        route = "billboard/{billboardId}",
                        arguments = listOf(
                            navArgument("billboardId") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val billboardId = backStackEntry.arguments?.getString("billboardId")
                        val billboard = billboards.find { it.id == billboardId } ?: billboards.firstOrNull()

                        if (billboard != null) {
                            BillboardScreen(
                                billboard = billboard,
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
