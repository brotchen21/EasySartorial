package com.brotchen21.easysatorial.domain.model

data class Garment(
    val id: Int,
    val name: String,
    val garmentTypeId: Int,
    val baseColor: String,
    val secondaryColor: String?,
    val colorFamily: ColorFamily,
    val patternId: Int,
    val patternScale: Int, // 0-4
    val patternContrast: Int,
    val patternType: String,
    val formalityLevel: Int, // 1-4
    val dressCodeCategory: String,
    val season: String,
    val fabricWeight: String,
    val imageAsset: String
)

enum class ColorFamily {
    NEUTRAL, EARTH, COOL, WARM, ACCENT
}

data class GarmentType(
    val id: Int,
    val name: String
)

data class Pattern(
    val id: Int,
    val name: String,
    val description: String,
    val coordinationAdvice: String,
    val imageUrl: String? = null
)
