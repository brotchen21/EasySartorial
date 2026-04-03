package com.brotchen21.easysatorial.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GarmentDto(
    val id: Int,
    val name: String? = null,
    @SerialName("garment_type_id")
    val garmentTypeId: Int? = null,
    @SerialName("base_color")
    val baseColor: String? = null,
    @SerialName("secondary_color")
    val secondaryColor: String? = null,
    @SerialName("color_family")
    val colorFamily: String? = null,
    @SerialName("pattern_id")
    val patternId: Int? = null,
    @SerialName("pattern_scale")
    val patternScale: Int? = null,
    @SerialName("pattern_contrast")
    val patternContrast: Int? = null,
    @SerialName("pattern_type")
    val patternType: String? = null,
    val season: String? = null,
    @SerialName("fabric_weight")
    val fabricWeight: String? = null,
    @SerialName("image_asset")
    val imageAsset: String? = null,
    @SerialName("color_tone")
    val colorTone: String? = null,
    @SerialName("color_temperature")
    val colorTemperature: String? = null,
    @SerialName("is_bold")
    val isBold: Boolean = false,
    @SerialName("color_role")
    val colorRole: String? = null,
    // Keep layering URLs as optional in case you added them later
    @SerialName("base_url")
    val baseUrl: String? = null,
    @SerialName("shading_url")
    val shadingUrl: String? = null,
    @SerialName("pattern_overlay_url")
    val patternOverlayUrl: String? = null
)

@Serializable
data class GarmentTypeDto(
    val id: Int,
    val name: String
)

@Serializable
data class PatternDto(
    val id: Int,
    val name: String? = null,
    @SerialName("pattern_type")
    val patternType: String? = null,
    val scale: Int? = null,
    val contrast: Int? = null,
    @SerialName("color_slots")
    val colorSlots: Int? = null
)
