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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Select Formality Level",
                style = MaterialTheme.typography.titleMedium
            )

            Slider(
                value = uiState.selectedFormality.toFloat(),
                onValueChange = { viewModel.setFormality(it.toInt()) },
                valueRange = 1f..4f,
                steps = 2
            )
            
            Text(
                text = when(uiState.selectedFormality) {
                    1 -> "Casual"
                    2 -> "Smart Casual"
                    3 -> "Business"
                    4 -> "Formal"
                    else -> ""
                },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Button(
                onClick = { viewModel.generateOutfit() },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Generate Random Outfit")
            }

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.currentOutfit != null) {
                uiState.validationResult?.let { result ->
                    OutfitStatusCard(result)
                }
            }
        }
    }
}
