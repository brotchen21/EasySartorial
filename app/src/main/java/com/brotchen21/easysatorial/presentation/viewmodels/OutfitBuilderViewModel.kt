package com.brotchen21.easysatorial.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brotchen21.easysatorial.domain.model.Garment
import com.brotchen21.easysatorial.domain.model.GarmentType
import com.brotchen21.easysatorial.domain.model.Outfit
import com.brotchen21.easysatorial.domain.model.OutfitValidationResult
import com.brotchen21.easysatorial.domain.usecase.GetGarmentTypesUseCase
import com.brotchen21.easysatorial.domain.usecase.GetGarmentsUseCase
import com.brotchen21.easysatorial.domain.usecase.ValidateOutfitUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OutfitBuilderViewModel(
    private val getGarmentTypesUseCase: GetGarmentTypesUseCase,
    private val getGarmentsUseCase: GetGarmentsUseCase,
    private val validateOutfitUseCase: ValidateOutfitUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OutfitBuilderUiState())
    val uiState: StateFlow<OutfitBuilderUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingGarments = true, errorMessage = null) }
            try {
                Log.d("OutfitBuilderVM", "Loading garment types...")
                val types = getGarmentTypesUseCase()
                Log.d("OutfitBuilderVM", "Loaded ${types.size} types")
                _uiState.update { it.copy(garmentTypes = types, isLoadingGarments = false) }
                
                if (types.isNotEmpty()) {
                    selectGarmentType(types[0])
                } else {
                    _uiState.update { it.copy(errorMessage = "No garment types found in database.") }
                }
            } catch (e: Exception) {
                Log.e("OutfitBuilderVM", "Failed to load types", e)
                _uiState.update { it.copy(isLoadingGarments = false, errorMessage = "Error: ${e.message}") }
            }
        }
    }

    fun selectGarmentType(type: GarmentType) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedTypeId = type.id, isLoadingGarments = true, errorMessage = null) }
            try {
                Log.d("OutfitBuilderVM", "Loading garments for type ${type.name} (ID: ${type.id})...")
                val garments = getGarmentsUseCase(type.id)
                Log.d("OutfitBuilderVM", "Loaded ${garments.size} garments")
                _uiState.update { it.copy(availableGarments = garments, isLoadingGarments = false) }
                if (garments.isEmpty()) {
                    _uiState.update { it.copy(errorMessage = "No garments found for ${type.name}.") }
                }
            } catch (e: Exception) {
                Log.e("OutfitBuilderVM", "Failed to load garments", e)
                _uiState.update { it.copy(isLoadingGarments = false, errorMessage = "Error loading garments: ${e.message}") }
            }
        }
    }

    fun selectGarment(garment: Garment) {
        _uiState.update { state ->
            val newSelectedGarments = state.selectedGarments.toMutableMap()
            newSelectedGarments[garment.garmentTypeId] = garment
            
            val newOutfit = Outfit(
                jacketId = newSelectedGarments[1]?.id ?: 0,
                shirtId = newSelectedGarments[3]?.id ?: 0,
                trousersId = newSelectedGarments[4]?.id ?: 0,
                shoesId = newSelectedGarments[7]?.id ?: 0,
                waistcoatId = newSelectedGarments[2]?.id ?: 0,
                tieId = newSelectedGarments[5]?.id ?: 0,
                beltId = newSelectedGarments[6]?.id ?: 0,
                sockId = newSelectedGarments[8]?.id ?: 0,
                hatId = newSelectedGarments[9]?.id ?: 0
            )
            state.copy(selectedGarments = newSelectedGarments, currentOutfit = newOutfit)
        }
        validateCurrentOutfit()
    }

    fun startOver() {
        _uiState.update { it.copy(selectedGarments = emptyMap(), currentOutfit = Outfit(0, 0, 0, 0), validationResult = null, errorMessage = null) }
    }

    fun toggleJacketVisibility() {
        _uiState.update { it.copy(isJacketVisible = !it.isJacketVisible) }
    }

    private fun validateCurrentOutfit() {
        viewModelScope.launch {
            try {
                val result = validateOutfitUseCase(_uiState.value.currentOutfit)
                _uiState.update { it.copy(validationResult = result) }
            } catch (e: Exception) {
                Log.e("OutfitBuilderVM", "Validation failed", e)
            }
        }
    }
}

data class OutfitBuilderUiState(
    val garmentTypes: List<GarmentType> = emptyList(),
    val selectedTypeId: Int? = null,
    val availableGarments: List<Garment> = emptyList(),
    val selectedGarments: Map<Int, Garment> = emptyMap(), // typeId to Garment
    val currentOutfit: Outfit = Outfit(0, 0, 0, 0),
    val validationResult: OutfitValidationResult? = null,
    val isLoadingGarments: Boolean = false,
    val isJacketVisible: Boolean = true,
    val errorMessage: String? = null
)
