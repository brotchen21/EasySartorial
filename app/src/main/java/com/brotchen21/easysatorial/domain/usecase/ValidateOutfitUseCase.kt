package com.brotchen21.easysatorial.domain.usecase

import com.brotchen21.easysatorial.domain.model.Outfit
import com.brotchen21.easysatorial.domain.model.OutfitValidationResult
import com.brotchen21.easysatorial.domain.repository.SartorialRepository

class ValidateOutfitUseCase(private val repository: SartorialRepository) {
    suspend operator fun invoke(outfit: Outfit): OutfitValidationResult {
        return repository.validateOutfit(outfit)
    }
}
