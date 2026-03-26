package com.brotchen21.easysatorial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.brotchen21.easysatorial.core.scoring.ScoringEngine
import com.brotchen21.easysatorial.data.repository.SartorialRepositoryImpl
import com.brotchen21.easysatorial.domain.usecase.*
import com.brotchen21.easysatorial.presentation.navigation.Screen
import com.brotchen21.easysatorial.presentation.screens.HomeScreen
import com.brotchen21.easysatorial.presentation.screens.OutfitBuilderScreen
import com.brotchen21.easysatorial.presentation.screens.PatternLibraryScreen
import com.brotchen21.easysatorial.presentation.screens.RandomGeneratorScreen
import com.brotchen21.easysatorial.presentation.viewmodels.OutfitBuilderViewModel
import com.brotchen21.easysatorial.presentation.viewmodels.PatternLibraryViewModel
import com.brotchen21.easysatorial.presentation.viewmodels.RandomGeneratorViewModel
import com.brotchen21.easysatorial.ui.theme.EasySartorialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Simple manual DI
        val scoringEngine = ScoringEngine()
        val repository = SartorialRepositoryImpl(scoringEngine)
        
        val getPatternsUseCase = GetPatternsUseCase(repository)
        val getGarmentTypesUseCase = GetGarmentTypesUseCase(repository)
        val getGarmentsUseCase = GetGarmentsUseCase(repository)
        val validateOutfitUseCase = ValidateOutfitUseCase(repository)
        val generateOutfitUseCase = GenerateOutfitUseCase(repository)

        setContent {
            EasySartorialTheme {
                val navController = rememberNavController()
                
                val patternLibraryViewModel = remember { PatternLibraryViewModel(getPatternsUseCase) }
                val outfitBuilderViewModel = remember { 
                    OutfitBuilderViewModel(getGarmentTypesUseCase, getGarmentsUseCase, validateOutfitUseCase) 
                }
                val randomGeneratorViewModel = remember {
                    RandomGeneratorViewModel(generateOutfitUseCase, validateOutfitUseCase)
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(
                                onNavigate = { route ->
                                    navController.navigate(route)
                                }
                            )
                        }
                        composable(Screen.PatternLibrary.route) {
                            PatternLibraryScreen(
                                viewModel = patternLibraryViewModel,
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable(Screen.OutfitBuilder.route) {
                            OutfitBuilderScreen(
                                viewModel = outfitBuilderViewModel,
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable(Screen.RandomGenerator.route) {
                            RandomGeneratorScreen(
                                viewModel = randomGeneratorViewModel,
                                onBackClick = {
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
