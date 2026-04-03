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
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(350.dp)
            ) {
                // Main Torso Area
                Box(
                    modifier = Modifier.size(250.dp, 350.dp),
                    contentAlignment = Alignment.Center
                ) {
                    GarmentStack(shirt)
                    GarmentStack(tie)
                    GarmentStack(waistcoat)
                    if (isJacketVisible) {
                        GarmentStack(jacket)
                    }
                }
                
                // Hat to the side
                Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
                    GarmentStack(hat)
                }
            }
            
            // Lower Body Area
            Box(
                modifier = Modifier.size(250.dp, 250.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(200.dp, 180.dp)) {
                        GarmentStack(trousers)
                    }
                    Box(modifier = Modifier.size(100.dp, 60.dp)) {
                        GarmentStack(shoes)
                    }
                }
            }
        }
    }
}

@Composable
private fun GarmentStack(garment: Garment?) {
    if (garment == null) return

    Box(modifier = Modifier.fillMaxSize()) {
        garment.baseUrl?.let { url ->
            AsyncImage(
                model = url,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
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
                contentScale = ContentScale.Fit
            )
        }

        garment.shadingUrl?.let { url ->
            AsyncImage(
                model = url,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
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
