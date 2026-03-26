package com.brotchen21.easysatorial.data.repository

import com.brotchen21.easysatorial.core.scoring.ScoringEngine
import com.brotchen21.easysatorial.domain.model.*
import com.brotchen21.easysatorial.domain.repository.SartorialRepository

class SartorialRepositoryImpl(private val scoringEngine: ScoringEngine) : SartorialRepository {
    private val mockGarments = listOf(
        Garment(1, "Navy Blazer", 1, "Navy", null, ColorFamily.COOL, 0, 0, 0, "Solid", 3, "Business", "all-season", "medium", ""),
        Garment(2, "Grey Flannel Trousers", 4, "Grey", null, ColorFamily.NEUTRAL, 0, 0, 0, "Solid", 3, "Business", "winter", "heavy", ""),
        Garment(3, "White Poplin Shirt", 3, "White", null, ColorFamily.NEUTRAL, 0, 0, 0, "Solid", 3, "Business", "all-season", "light", ""),
        Garment(4, "Brown Oxford Shoes", 7, "Brown", null, ColorFamily.EARTH, 0, 0, 0, "Solid", 3, "Business", "all-season", "medium", ""),
        Garment(5, "Brown Leather Belt", 6, "Brown", null, ColorFamily.EARTH, 0, 0, 0, "Solid", 3, "Business", "all-season", "medium", ""),
        Garment(6, "Blue Silk Tie", 5, "Blue", null, ColorFamily.COOL, 0, 0, 0, "Solid", 4, "Formal", "all-season", "light", ""),
        Garment(7, "Navy Cotton Socks", 8, "Navy", null, ColorFamily.COOL, 0, 0, 0, "Solid", 2, "Smart Casual", "all-season", "light", "")
    )

    override suspend fun getGarmentTypes(): List<GarmentType> {
        return listOf(
            GarmentType(1, "Jacket"),
            GarmentType(2, "Waistcoat"),
            GarmentType(3, "Shirt"),
            GarmentType(4, "Trousers"),
            GarmentType(5, "Tie"),
            GarmentType(6, "Belt"),
            GarmentType(7, "Shoes"),
            GarmentType(8, "Socks"),
            GarmentType(9, "Hat")
        )
    }

    override suspend fun getGarments(typeId: Int): List<Garment> {
        return mockGarments.filter { it.garmentTypeId == typeId }
    }

    override suspend fun getPatterns(): List<Pattern> {
        return listOf(
            Pattern(1, "Pinstripe", "Vertical lines, typically thin and evenly spaced.", "Pair with solid shirts or larger scale patterns like Glen Check."),
            Pattern(2, "Glen Check", "A woolen fabric with a design of small and large checks.", "Pairs well with solid ties or simple stripes."),
            Pattern(3, "Herringbone", "A V-shaped weaving pattern.", "Very versatile; works with almost any other pattern."),
            Pattern(4, "Houndstooth", "Broken checks or four-pointed shapes.", "Keep other patterns simple to avoid visual clutter."),
            Pattern(5, "Windowpane", "Large, thin-lined checks.", "Contrasts beautifully with small-scale patterns like micro-checks.")
        )
    }

    override suspend fun generateOutfit(formality: Int): Outfit {
        // Return a basic valid outfit matching the formality
        return Outfit(
            jacketId = 1,
            shirtId = 3,
            trousersId = 2,
            shoesId = 4,
            beltId = 5,
            tieId = 6,
            sockId = 7
        )
    }

    override suspend fun validateOutfit(outfit: Outfit): OutfitValidationResult {
        val jacket = mockGarments.find { it.id == outfit.jacketId } ?: mockGarments[0]
        val shirt = mockGarments.find { it.id == outfit.shirtId } ?: mockGarments[2]
        val trousers = mockGarments.find { it.id == outfit.trousersId } ?: mockGarments[1]
        val shoes = mockGarments.find { it.id == outfit.shoesId } ?: mockGarments[3]
        val belt = mockGarments.find { it.id == outfit.beltId }
        val socks = mockGarments.find { it.id == outfit.sockId }
        val tie = mockGarments.find { it.id == outfit.tieId }
        val waistcoat = mockGarments.find { it.id == outfit.waistcoatId }
        val hat = mockGarments.find { it.id == outfit.hatId }

        return scoringEngine.calculateScore(
            jacket, shirt, trousers, shoes, belt, socks, tie, waistcoat, hat
        )
    }
}
