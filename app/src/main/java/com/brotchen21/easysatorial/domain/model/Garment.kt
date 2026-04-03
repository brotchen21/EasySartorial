package com.brotchen21.easysatorial.domain.model

data class Garment(
    val id: Int,
    val name: String,
    val garmentTypeId: Int,
    val colorProfile: ColorProfile,
    val secondaryColor: String?,
    val colorFamily: ColorFamily,
    val patternId: Int,
    val patternScale: Int,
    val patternContrast: Int,
    val patternType: String,
    val season: String,
    val fabricWeight: String,
    val imageAsset: String,
    val isBold: Boolean,
    val colorRole: String?,
    // Mannequin Layering URLs (Stored in Storage, paths in DB)
    val baseUrl: String? = null,
    val shadingUrl: String? = null,
    val patternOverlayUrl: String? = null
) {
    val baseColor: String get() = colorProfile.base.name
}

data class ColorProfile(
    val base: ColorBase,
    val tone: ColorTone,
    val temperature: ColorTemperature
)

enum class ColorBase(val isNeutral: Boolean) {
    NAVY(true), GREY(true), BROWN(true), WHITE(true), BLACK(true),
    RED(false), GREEN(false), MUSTARD(false), BRIGHT_BLUE(false);
}

enum class ColorTone {
    LIGHT, MEDIUM, DARK
}

enum class ColorTemperature {
    WARM, COOL, NEUTRAL
}

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
    val patternType: String,
    val scale: Int,
    val contrast: Int,
    val colorSlots: Int
)
