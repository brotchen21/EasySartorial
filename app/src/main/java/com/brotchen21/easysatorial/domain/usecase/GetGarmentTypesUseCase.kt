package com.brotchen21.easysatorial.domain.usecase

import com.brotchen21.easysatorial.domain.model.GarmentType
import com.brotchen21.easysatorial.domain.repository.SartorialRepository

class GetGarmentTypesUseCase(private val repository: SartorialRepository) {
    suspend operator fun invoke(): List<GarmentType> {
        return repository.getGarmentTypes()
    }
}
