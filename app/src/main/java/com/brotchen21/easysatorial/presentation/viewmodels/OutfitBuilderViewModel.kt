package com.brotchen21.easysatorial.presentation.viewmodels

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
            val types = getGarmentTypesUseCase()
            _uiState.update { it.copy(garmentTypes = types) }
            
            // Load garments for the first type by default
            if (types.isNotEmpty()) {
                selectGarmentType(types[0])
            }
        }
    }

    fun selectGarmentType(type: GarmentType) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedTypeId = type.id, isLoadingGarments = true) }
            val garments = getGarmentsUseCase(type.id)
            _uiState.update { it.copy(availableGarments = garments, isLoadingGarments = false) }
        }
    }

    fun selectGarment(garment: Garment) {
        _uiState.update { state ->
            val newOutfit = when (garment.garmentTypeId) {
                1 -> state.currentOutfit.copy(jacketId = garment.id)
                2 -> state.currentOutfit.copy(waistcoatId = garment.id)
                3 -> state.currentOutfit.copy(shirtId = garment.id)
                4 -> state.currentOutfit.copy(trousersId = garment.id)
                5 -> state.currentOutfit.copy(tieId = garment.id)
                6 -> state.currentOutfit.copy(beltId = garment.id)
                7 -> state.currentOutfit.copy(shoesId = garment.id)
                8 -> state.currentOutfit.copy(sockId = garment.id)
                9 -> state.currentOutfit.copy(hatId = garment.id)
                else -> state.currentOutfit
            }
            state.copy(currentOutfit = newOutfit)
        }
        validateCurrentOutfit()
    }

    private fun validateCurrentOutfit() {
        viewModelScope.launch {
            val result = validateOutfitUseCase(_uiState.value.currentOutfit)
            _uiState.update { it.copy(validationResult = result) }
        }
    }
}

data class OutfitBuilderUiState(
    val garmentTypes: List<GarmentType> = emptyList(),
    val selectedTypeId: Int? = null,
    val availableGarments: List<Garment> = emptyList(),
    val currentOutfit: Outfit = Outfit(0, 0, 0, 0),
    val validationResult: OutfitValidationResult? = null,
    val isLoadingGarments: Boolean = false
)
