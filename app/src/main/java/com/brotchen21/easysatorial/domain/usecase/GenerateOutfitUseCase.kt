package com.brotchen21.easysatorial.domain.usecase

import com.brotchen21.easysatorial.domain.model.Outfit
import com.brotchen21.easysatorial.domain.repository.SartorialRepository

class GenerateOutfitUseCase(private val repository: SartorialRepository) {
    suspend operator fun invoke(): Outfit {
        return repository.generateOutfit()
    }
}
