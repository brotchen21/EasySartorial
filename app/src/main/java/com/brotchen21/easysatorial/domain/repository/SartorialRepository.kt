package com.brotchen21.easysatorial.domain.repository

import com.brotchen21.easysatorial.domain.model.Garment
import com.brotchen21.easysatorial.domain.model.GarmentType
import com.brotchen21.easysatorial.domain.model.Outfit
import com.brotchen21.easysatorial.domain.model.OutfitValidationResult
import com.brotchen21.easysatorial.domain.model.Pattern

interface SartorialRepository {
    suspend fun getGarmentTypes(): List<GarmentType>
    suspend fun getGarments(typeId: Int): List<Garment>
    suspend fun getPatterns(): List<Pattern>
    suspend fun generateOutfit(formality: Int): Outfit
    suspend fun validateOutfit(outfit: Outfit): OutfitValidationResult
}
