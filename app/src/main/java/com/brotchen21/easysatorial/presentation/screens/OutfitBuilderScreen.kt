package com.brotchen21.easysatorial.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.brotchen21.easysatorial.domain.model.Garment
import com.brotchen21.easysatorial.domain.model.GarmentType
import com.brotchen21.easysatorial.domain.model.OutfitValidationResult
import com.brotchen21.easysatorial.presentation.viewmodels.OutfitBuilderViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutfitBuilderScreen(
    viewModel: OutfitBuilderViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Outfit Builder") },
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
        ) {
            // Garment Type Selector
            GarmentTypeSelector(
                types = uiState.garmentTypes,
                selectedTypeId = uiState.selectedTypeId,
                onTypeSelected = { viewModel.selectGarmentType(it) }
            )

            HorizontalDivider()

            // Available Garments List
            Box(modifier = Modifier.weight(1f)) {
                if (uiState.isLoadingGarments) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    GarmentSelectionList(
                        garments = uiState.availableGarments,
                        onGarmentSelected = { viewModel.selectGarment(it) }
                    )
                }
            }

            // Current Outfit Status / Score
            uiState.validationResult?.let { result ->
                OutfitStatusCard(result)
            }
        }
    }
}

@Composable
fun GarmentTypeSelector(
    types: List<GarmentType>,
    selectedTypeId: Int?,
    onTypeSelected: (GarmentType) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(types) { type ->
            FilterChip(
                selected = type.id == selectedTypeId,
                onClick = { onTypeSelected(type) },
                label = { Text(type.name) }
            )
        }
    }
}

@Composable
fun GarmentSelectionList(
    garments: List<Garment>,
    onGarmentSelected: (Garment) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(garments) { garment ->
            GarmentCard(garment, onGarmentSelected)
        }
    }
}

@Composable
fun GarmentCard(garment: Garment, onSelected: (Garment) -> Unit) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(200.dp)
            .clickable { onSelected(garment) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Gray.copy(alpha = 0.2f), MaterialTheme.shapes.small)
            ) {
                Text(
                    text = garment.name.take(1),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = garment.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text(
                text = "Formality: ${garment.formalityLevel}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun OutfitStatusCard(result: OutfitValidationResult) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Outfit Score",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = String.format(Locale.getDefault(), "%.1f/10", result.score),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            result.feedback.take(2).forEach { message ->
                Text(
                    text = "• $message",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
