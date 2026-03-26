package com.brotchen21.easysatorial.domain.usecase

import com.brotchen21.easysatorial.domain.model.Garment
import com.brotchen21.easysatorial.domain.repository.SartorialRepository

class GetGarmentsUseCase(private val repository: SartorialRepository) {
    suspend operator fun invoke(typeId: Int): List<Garment> {
        return repository.getGarments(typeId)
    }
}
