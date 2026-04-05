package com.brotchen21.easysatorial.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.brotchen21.easysatorial.domain.model.Garment

@Composable
fun SartorialMannequin(
    modifier: Modifier = Modifier,
    shirt: Garment? = null,
    tie: Garment? = null,
    trousers: Garment? = null,
    jacket: Garment? = null,
    waistcoat: Garment? = null,
    hat: Garment? = null,
    shoes: Garment? = null,
    isJacketVisible: Boolean = true
) {
    // Spread-out collage layout to avoid messy overlaps with current assets
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Left Column: Jacket & Shoes
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth().aspectRatio(0.6f)) {
                if (isJacketVisible) GarmentStack(jacket)
            }
            Box(modifier = Modifier.fillMaxWidth(0.8f).aspectRatio(1f)) {
                GarmentStack(shoes)
            }
        }

        // Middle Column: Trousers & Hat
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth().aspectRatio(0.5f)) {
                GarmentStack(trousers)
            }
            Box(modifier = Modifier.fillMaxWidth(0.7f).aspectRatio(1.2f)) {
                GarmentStack(hat)
            }
        }

        // Right Column: Shirt, Waistcoat, Tie
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth().aspectRatio(0.6f)) {
                GarmentStack(shirt)
            }
            Box(modifier = Modifier.fillMaxWidth(0.8f).aspectRatio(1f)) {
                GarmentStack(waistcoat)
            }
            Box(modifier = Modifier.fillMaxWidth(0.6f).aspectRatio(0.8f)) {
                GarmentStack(tie)
            }
        }
    }
}

@Composable
private fun GarmentStack(garment: Garment?) {
    if (garment == null) return

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        garment.baseUrl?.let { url ->
            AsyncImage(
                model = url,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                // Use FillHeight and CenterStart to effectively "crop" the empty right side 
                // of the 1024x1024 assets where the garment is pushed to the left.
                contentScale = ContentScale.FillHeight,
                alignment = Alignment.CenterStart,
                colorFilter = ColorFilter.tint(
                    color = parseColor(garment.baseColor), 
                    blendMode = BlendMode.Modulate
                )
            )
        }

        garment.patternOverlayUrl?.let { url ->
            AsyncImage(
                model = url,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillHeight,
                alignment = Alignment.CenterStart
            )
        }

        garment.shadingUrl?.let { url ->
            AsyncImage(
                model = url,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillHeight,
                alignment = Alignment.CenterStart,
                colorFilter = ColorFilter.tint(Color.White, BlendMode.Multiply)
            )
        }
    }
}

private fun parseColor(colorString: String): Color {
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
