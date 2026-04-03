package com.brotchen21.easysatorial.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brotchen21.easysatorial.domain.model.Outfit
import com.brotchen21.easysatorial.domain.model.OutfitValidationResult
import com.brotchen21.easysatorial.domain.usecase.GenerateOutfitUseCase
import com.brotchen21.easysatorial.domain.usecase.ValidateOutfitUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RandomGeneratorViewModel(
    private val generateOutfitUseCase: GenerateOutfitUseCase,
    private val validateOutfitUseCase: ValidateOutfitUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RandomGeneratorUiState())
    val uiState: StateFlow<RandomGeneratorUiState> = _uiState.asStateFlow()

    fun generateOutfit() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val outfit = generateOutfitUseCase()
            val validation = validateOutfitUseCase(outfit)
            _uiState.update { 
                it.copy(
                    currentOutfit = outfit,
                    validationResult = validation,
                    isLoading = false
                )
            }
        }
    }
}

data class RandomGeneratorUiState(
    val currentOutfit: Outfit? = null,
    val validationResult: OutfitValidationResult? = null,
    val isLoading: Boolean = false
)
