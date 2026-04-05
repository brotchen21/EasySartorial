package com.brotchen21.easysatorial.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.brotchen21.easysatorial.presentation.components.OutfitStatusCard
import com.brotchen21.easysatorial.presentation.components.SartorialMannequin
import com.brotchen21.easysatorial.presentation.viewmodels.RandomGeneratorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomGeneratorScreen(
    viewModel: RandomGeneratorViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Random Generator") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Generate a classically coordinated outfit based on 1930s-1940s tailoring rules.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // MANNEQUIN AREA for Random Generator
            SartorialMannequin(
                modifier = Modifier.weight(1f),
                shirt = uiState.currentOutfitGarments?.get(3),
                tie = uiState.currentOutfitGarments?.get(5),
                trousers = uiState.currentOutfitGarments?.get(4),
                jacket = uiState.currentOutfitGarments?.get(1),
                waistcoat = uiState.currentOutfitGarments?.get(2),
                hat = uiState.currentOutfitGarments?.get(9),
                shoes = uiState.currentOutfitGarments?.get(7),
                isJacketVisible = true
            )

            Button(
                onClick = { viewModel.generateOutfit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(56.dp)
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Generate Outfit")
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
            } else if (uiState.currentOutfit != null) {
                uiState.validationResult?.let { result ->
                    OutfitStatusCard(result)
                }
            }
        }
    }
}
