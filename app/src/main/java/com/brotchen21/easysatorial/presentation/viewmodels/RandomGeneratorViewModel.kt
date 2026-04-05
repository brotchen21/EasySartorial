package com.brotchen21.easysatorial.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brotchen21.easysatorial.domain.model.Garment
import com.brotchen21.easysatorial.domain.model.Outfit
import com.brotchen21.easysatorial.domain.model.OutfitValidationResult
import com.brotchen21.easysatorial.domain.usecase.GenerateOutfitUseCase
import com.brotchen21.easysatorial.domain.usecase.GetGarmentsUseCase
import com.brotchen21.easysatorial.domain.usecase.ValidateOutfitUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RandomGeneratorViewModel(
    private val generateOutfitUseCase: GenerateOutfitUseCase,
    private val getGarmentsUseCase: GetGarmentsUseCase,
    private val validateOutfitUseCase: ValidateOutfitUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RandomGeneratorUiState())
    val uiState: StateFlow<RandomGeneratorUiState> = _uiState.asStateFlow()

    fun generateOutfit() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val outfit = generateOutfitUseCase()
            val validation = validateOutfitUseCase(outfit)
            
            // Map IDs back to Garments for display
            // In a real app, you might want a more efficient way to fetch these
            val garmentMap = mutableMapOf<Int, Garment>()
            
            // This is a bit inefficient but works for now
            listOf(1, 2, 3, 4, 5, 7, 9).forEach { typeId ->
                val garments = getGarmentsUseCase(typeId)
                val selectedId = when(typeId) {
                    1 -> outfit.jacketId
                    2 -> outfit.waistcoatId
                    3 -> outfit.shirtId
                    4 -> outfit.trousersId
                    5 -> outfit.tieId
                    7 -> outfit.shoesId
                    9 -> outfit.hatId
                    else -> null
                }
                garments.find { it.id == selectedId }?.let {
                    garmentMap[typeId] = it
                }
            }

            _uiState.update { 
                it.copy(
                    currentOutfit = outfit,
                    currentOutfitGarments = garmentMap,
                    validationResult = validation,
                    isLoading = false
                )
            }
        }
    }
}

data class RandomGeneratorUiState(
    val currentOutfit: Outfit? = null,
    val currentOutfitGarments: Map<Int, Garment>? = null,
    val validationResult: OutfitValidationResult? = null,
    val isLoading: Boolean = false
)
