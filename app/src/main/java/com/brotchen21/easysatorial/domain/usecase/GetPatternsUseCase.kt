package com.brotchen21.easysatorial.domain.usecase

import com.brotchen21.easysatorial.domain.model.Pattern
import com.brotchen21.easysatorial.domain.repository.SartorialRepository

class GetPatternsUseCase(private val repository: SartorialRepository) {
    suspend operator fun invoke(): List<Pattern> {
        return repository.getPatterns()
    }
}
