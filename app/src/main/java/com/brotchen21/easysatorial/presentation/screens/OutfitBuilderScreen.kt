package com.brotchen21.easysatorial.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brotchen21.easysatorial.domain.model.Garment
import com.brotchen21.easysatorial.domain.model.GarmentType
import com.brotchen21.easysatorial.presentation.components.SartorialMannequin
import com.brotchen21.easysatorial.presentation.viewmodels.OutfitBuilderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutfitBuilderScreen(
    viewModel: OutfitBuilderViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color(0xFFF5F9FF),
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.startOver() }) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(20.dp))
                            Text("Start over", fontSize = 10.sp)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Error Display (If data fails to load)
            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

            // 2. MANNEQUIN AREA
            SartorialMannequin(
                modifier = Modifier.weight(1f),
                shirt = uiState.selectedGarments[3],
                tie = uiState.selectedGarments[5],
                trousers = uiState.selectedGarments[4],
                jacket = uiState.selectedGarments[1],
                waistcoat = uiState.selectedGarments[2],
                hat = uiState.selectedGarments[9],
                shoes = uiState.selectedGarments[7],
                isJacketVisible = uiState.isJacketVisible
            )

            // 3. TOGGLE BUTTON
            OutlinedButton(
                onClick = { viewModel.toggleJacketVisibility() },
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(if (uiState.isJacketVisible) "Hide Jacket" else "Show Jacket")
            }

            // 4. CATEGORY TABS
            GarmentCategoryTabs(
                types = uiState.garmentTypes,
                selectedTypeId = uiState.selectedTypeId,
                onTypeSelected = { viewModel.selectGarmentType(it) }
            )

            // 5. COLOR/ITEM SELECTION ROW
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.White)
            ) {
                if (uiState.isLoadingGarments) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (uiState.availableGarments.isNotEmpty()) {
                    GarmentColorSelectionList(
                        garments = uiState.availableGarments,
                        selectedGarment = uiState.selectedGarments[uiState.selectedTypeId],
                        onGarmentSelected = { viewModel.selectGarment(it) }
                    )
                } else if (uiState.errorMessage == null) {
                    Text(
                        "No items available in this category.",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun GarmentCategoryTabs(
    types: List<GarmentType>,
    selectedTypeId: Int?,
    onTypeSelected: (GarmentType) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth().background(Color.White).padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(types) { type ->
            val isSelected = type.id == selectedTypeId
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isSelected) Color(0xFFE9A11E) else Color.Transparent)
                    .clickable { onTypeSelected(type) }
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = type.name,
                    color = if (isSelected) Color.White else Color.Gray,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun GarmentColorSelectionList(
    garments: List<Garment>,
    selectedGarment: Garment?,
    onGarmentSelected: (Garment) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(garments) { garment ->
            val isSelected = garment.id == selectedGarment?.id
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(70.dp).clickable { onGarmentSelected(garment) }
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(parseColorForUI(garment.baseColor))
                        .border(2.dp, if (isSelected) Color.Black else Color.Transparent, CircleShape)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = garment.name,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 12.sp,
                    maxLines = 2,
                    color = if (isSelected) Color.Black else Color.Gray
                )
            }
        }
    }
}

private fun parseColorForUI(colorString: String): Color {
    return try {
        if (colorString.startsWith("#")) {
            Color(android.graphics.Color.parseColor(colorString))
        } else {
            when(colorString.lowercase()) {
                "navy" -> Color(0xFF1A237E)
                "white" -> Color.White
                "burgundy" -> Color(0xFF880E4F)
                "charcoal" -> Color(0xFF263238)
                "camel" -> Color(0xFFC19A6B)
                "light grey" -> Color(0xFFD3D3D3)
                "light brown" -> Color(0xFFB5651D)
                "mid brown" -> Color(0xFF8B4513)
                "dark brown" -> Color(0xFF5D4037)
                "sky blue" -> Color(0xFF87CEEB)
                else -> Color.Gray
            }
        }
    } catch (e: Exception) {
        Color.LightGray
    }
}
