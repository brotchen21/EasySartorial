package com.brotchen21.easysatorial.core.scoring

import com.brotchen21.easysatorial.domain.model.*
import kotlin.math.abs

class ScoringEngine {

    fun calculateScore(
        jacket: Garment,
        shirt: Garment,
        trousers: Garment,
        shoes: Garment?,
        belt: Garment?,
        socks: Garment?,
        tie: Garment?,
        waistcoat: Garment?,
        hat: Garment?
    ): OutfitValidationResult {
        val feedback = mutableListOf<String>()
        var totalScore = 0f

        // 1. Foundation Rule (Jacket + Trousers)
        val foundationScore = calculateFoundationScore(jacket, trousers, feedback)
        totalScore += foundationScore

        // 2. Shirt Contrast Rule
        val shirtContrastScore = calculateShirtContrastScore(jacket, shirt, feedback)
        totalScore += shirtContrastScore

        // 3. Tie Harmony Rule
        if (tie != null) {
            totalScore += calculateTieHarmonyScore(jacket, shirt, tie, feedback)
        }

        // 4. Color Balance Rule (Using DB 'isBold' field)
        totalScore += calculateColorBalanceScore(listOfNotNull(jacket, shirt, trousers, tie, waistcoat, hat), feedback)

        // 5. Temperature Rule
        totalScore += calculateTemperatureScore(listOfNotNull(jacket, shirt, trousers, tie, shoes), feedback)

        // 6. Pattern + Color Interaction
        val patternScore = calculatePatternColorInteraction(jacket, shirt, trousers, tie, feedback)
        totalScore += patternScore

        // 7. Accessory Match (Shoe & Belt)
        if (belt != null && shoes != null) {
            totalScore += calculateAccessoryMatchScore(shoes, belt, feedback)
        } else if (belt != null) {
            feedback.add("Note: Wear matching shoes with your belt.")
        }

        // Final score normalization (assuming base score starts at 0, max is 10)
        val finalScore = (5f + totalScore).coerceIn(0f, 10f)

        return OutfitValidationResult(
            score = finalScore,
            colorScore = (5f + totalScore - patternScore).coerceIn(0f, 10f),
            patternScore = (5f + patternScore).coerceIn(0f, 10f),
            feedback = feedback
        )
    }

    private fun calculateFoundationScore(jacket: Garment, trousers: Garment, feedback: MutableList<String>): Float {
        val j = jacket.colorProfile
        val t = trousers.colorProfile

        return when {
            j.base == t.base -> {
                feedback.add("Perfect foundation: Matching jacket and trousers.")
                2.0f
            }
            j.base.isNeutral && t.base.isNeutral -> {
                feedback.add("Solid foundation: Coordinating neutral tones.")
                1.5f
            }
            else -> {
                feedback.add("Warning: Jacket and trousers have low color compatibility.")
                -0.5f
            }
        }
    }

    private fun calculateShirtContrastScore(jacket: Garment, shirt: Garment, feedback: MutableList<String>): Float {
        val jTone = jacket.colorProfile.tone
        val sTone = shirt.colorProfile.tone

        return when {
            sTone == ColorTone.LIGHT && jTone == ColorTone.DARK -> {
                feedback.add("Ideal contrast: Light shirt with a dark jacket.")
                2.0f
            }
            sTone == ColorTone.DARK && jTone == ColorTone.DARK -> {
                feedback.add("Poor contrast: Dark shirt under a dark jacket lacks definition.")
                -1.0f
            }
            else -> 1.0f
        }
    }

    private fun calculateTieHarmonyScore(jacket: Garment, shirt: Garment, tie: Garment, feedback: MutableList<String>): Float {
        val tBase = tie.colorProfile.base
        val jBase = jacket.colorProfile.base
        
        val relatesToJacket = tBase == jBase || tie.colorFamily == jacket.colorFamily
        val relatesToShirt = tie.colorProfile.temperature != shirt.colorProfile.temperature // Simple complementary logic

        return when {
            relatesToJacket -> {
                feedback.add("Tie harmony: Tie color relates well to the jacket.")
                1.5f
            }
            relatesToShirt -> {
                feedback.add("Tie harmony: Tie provides a nice contrast to the shirt.")
                1.0f
            }
            else -> {
                feedback.add("Penalty: Tie clashes with both jacket and shirt.")
                -1.0f
            }
        }
    }

    private fun calculateColorBalanceScore(items: List<Garment>, feedback: MutableList<String>): Float {
        // Now using the explicit 'isBold' field from your database
        val boldItems = items.filter { it.isBold }
        
        return when {
            boldItems.size > 1 -> {
                feedback.add("Penalty: Too many bold colors (${boldItems.joinToString { it.name }}). Limit to one focus piece.")
                -1.5f
            }
            boldItems.size == 1 -> {
                feedback.add("Good balance: Single bold ${boldItems[0].name} supported by neutrals.")
                1.5f
            }
            else -> 1.0f
        }
    }

    private fun calculateTemperatureScore(items: List<Garment>, feedback: MutableList<String>): Float {
        val temps = items.map { it.colorProfile.temperature }.filter { it != ColorTemperature.NEUTRAL }
        val hasWarm = temps.contains(ColorTemperature.WARM)
        val hasCool = temps.contains(ColorTemperature.COOL)

        return if (hasWarm && hasCool) {
            feedback.add("Penalty: Clashing temperatures between warm and cool items.")
            -1.0f
        } else {
            1.0f
        }
    }

    private fun calculatePatternColorInteraction(jacket: Garment, shirt: Garment, trousers: Garment, tie: Garment?, feedback: MutableList<String>): Float {
        val items = listOfNotNull(jacket, shirt, trousers, tie)
        val loudPatterns = items.filter { it.patternScale >= 3 }
        
        return when {
            loudPatterns.size > 1 -> {
                feedback.add("Penalty: Multiple loud patterns compete for attention.")
                -2.0f
            }
            loudPatterns.size == 1 -> {
                val othersAreNeutral = items.filter { it != loudPatterns[0] }.all { it.colorProfile.base.isNeutral }
                if (othersAreNeutral) {
                    feedback.add("Pattern balance: Loud ${loudPatterns[0].name} is correctly supported by neutrals.")
                    1.5f
                } else {
                    0.5f
                }
            }
            else -> 1.0f
        }
    }

    private fun calculateAccessoryMatchScore(shoes: Garment, belt: Garment, feedback: MutableList<String>): Float {
        return if (shoes.colorProfile.base == belt.colorProfile.base) {
            feedback.add("Accessory match: Belt and shoes are correctly coordinated.")
            1.0f
        } else {
            feedback.add("Penalty: Belt and shoes should match in base color.")
            -1.0f
        }
    }
}
