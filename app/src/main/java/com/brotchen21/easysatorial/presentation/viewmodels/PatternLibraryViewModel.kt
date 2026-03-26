package com.brotchen21.easysatorial.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brotchen21.easysatorial.domain.model.Pattern
import com.brotchen21.easysatorial.domain.usecase.GetPatternsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PatternLibraryViewModel(
    private val getPatternsUseCase: GetPatternsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PatternLibraryUiState>(PatternLibraryUiState.Loading)
    val uiState: StateFlow<PatternLibraryUiState> = _uiState.asStateFlow()

    init {
        loadPatterns()
    }

    private fun loadPatterns() {
        viewModelScope.launch {
            _uiState.value = PatternLibraryUiState.Loading
            try {
                val patterns = getPatternsUseCase()
                _uiState.value = PatternLibraryUiState.Success(patterns)
            } catch (e: Exception) {
                _uiState.value = PatternLibraryUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class PatternLibraryUiState {
    object Loading : PatternLibraryUiState()
    data class Success(val patterns: List<Pattern>) : PatternLibraryUiState()
    data class Error(val message: String) : PatternLibraryUiState()
}
