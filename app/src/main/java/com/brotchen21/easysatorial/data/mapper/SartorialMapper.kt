package com.brotchen21.easysatorial.data.mapper

import com.brotchen21.easysatorial.data.remote.dto.GarmentDto
import com.brotchen21.easysatorial.data.remote.dto.GarmentTypeDto
import com.brotchen21.easysatorial.data.remote.dto.PatternDto
import com.brotchen21.easysatorial.domain.model.*

fun GarmentDto.toDomain(): Garment {
    return Garment(
        id = id,
        name = name ?: "Unknown",
        garmentTypeId = garmentTypeId ?: 0,
        colorProfile = ColorProfile(
            base = try { ColorBase.valueOf(baseColor?.uppercase() ?: "GREY") } catch (e: Exception) { ColorBase.GREY },
            tone = try { ColorTone.valueOf(colorTone?.uppercase() ?: "MEDIUM") } catch (e: Exception) { ColorTone.MEDIUM },
            temperature = try { ColorTemperature.valueOf(colorTemperature?.uppercase() ?: "NEUTRAL") } catch (e: Exception) { ColorTemperature.NEUTRAL }
        ),
        secondaryColor = secondaryColor,
        colorFamily = try { ColorFamily.valueOf(colorFamily?.uppercase() ?: "NEUTRAL") } catch (e: Exception) { ColorFamily.NEUTRAL },
        patternId = patternId ?: 0,
        patternScale = patternScale ?: 0,
        patternContrast = patternContrast ?: 0,
        patternType = patternType ?: "Solid",
        season = season ?: "all-season",
        fabricWeight = fabricWeight ?: "medium",
        imageAsset = imageAsset ?: "",
        isBold = isBold,
        colorRole = colorRole,
        baseUrl = baseUrl,
        shadingUrl = shadingUrl,
        patternOverlayUrl = patternOverlayUrl
    )
}

fun GarmentTypeDto.toDomain(): GarmentType {
    return GarmentType(
        id = id,
        name = name
    )
}

fun PatternDto.toDomain(): Pattern {
    return Pattern(
        id = id,
        name = name ?: "Unknown",
        patternType = patternType ?: "Unknown",
        scale = scale ?: 0,
        contrast = contrast ?: 0,
        colorSlots = colorSlots ?: 0
    )
}
