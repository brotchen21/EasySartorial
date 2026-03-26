package com.brotchen21.easysatorial.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.brotchen21.easysatorial.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Easy Sartorial") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HomeButton(
                text = "Outfit Builder",
                icon = Icons.Default.Style,
                onClick = { onNavigate(Screen.OutfitBuilder.route) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            HomeButton(
                text = "Random Generator",
                icon = Icons.Default.AutoAwesome,
                onClick = { onNavigate(Screen.RandomGenerator.route) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            HomeButton(
                text = "Pattern Library",
                icon = Icons.AutoMirrored.Filled.LibraryBooks,
                onClick = { onNavigate(Screen.PatternLibrary.route) }
            )
        }
    }
}

@Composable
fun HomeButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}
