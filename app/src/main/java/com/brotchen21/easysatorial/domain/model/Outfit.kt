package com.brotchen21.easysatorial.domain.model

data class Outfit(
    val jacketId: Int,
    val shirtId: Int,
    val trousersId: Int,
    val shoesId: Int? = null,
    val hatId: Int? = null,
    val waistcoatId: Int? = null,
    val tieId: Int? = null,
    val beltId: Int? = null,
    val sockId: Int? = null
)

data class OutfitValidationResult(
    val score: Float,
    val colorScore: Float,
    val patternScore: Float,
    val feedback: List<String>
)
