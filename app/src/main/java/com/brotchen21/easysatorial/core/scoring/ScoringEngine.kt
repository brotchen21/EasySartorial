package com.brotchen21.easysatorial.core.scoring

import com.brotchen21.easysatorial.domain.model.Garment
import com.brotchen21.easysatorial.domain.model.OutfitValidationResult

class ScoringEngine {

    fun calculateScore(
        jacket: Garment,
        shirt: Garment,
        trousers: Garment,
        shoes: Garment,
        belt: Garment?,
        socks: Garment?,
        tie: Garment?,
        waistcoat: Garment?,
        hat: Garment?
    ): OutfitValidationResult {
        val feedback = mutableListOf<String>()
        
        val colorScore = calculateColorScore(jacket, shirt, trousers, shoes, belt, socks, feedback)
        val patternScore = calculatePatternScore(jacket, shirt, trousers, tie, feedback)
        val formalityScore = calculateFormalityScore(jacket, shirt, trousers, shoes, tie, feedback)
        
        val totalScore = (colorScore * ScoringConstants.COLOR_HARMONY_WEIGHT) +
                (patternScore * ScoringConstants.PATTERN_BALANCE_WEIGHT) +
                (formalityScore * ScoringConstants.FORMALITY_MATCH_WEIGHT)
                
        return OutfitValidationResult(
            score = totalScore,
            colorScore = colorScore,
            patternScore = patternScore,
            formalityScore = formalityScore,
            feedback = feedback
        )
    }

    private fun calculateColorScore(
        jacket: Garment,
        shirt: Garment,
        trousers: Garment,
        shoes: Garment,
        belt: Garment?,
        socks: Garment?,
        feedback: MutableList<String>
    ): Float {
        var score = 10.0f
        
        // Belt-Shoe match rule
        if (belt != null) {
            if (belt.baseColor != shoes.baseColor) {
                score -= 2.0f
                feedback.add("Belt color (${belt.baseColor}) does not match shoe color (${shoes.baseColor}).")
            } else {
                feedback.add("Belt correctly matches shoe color.")
            }
        }
        
        // Sock rules
        if (socks != null) {
            val matchesTrousers = socks.baseColor == trousers.baseColor
            val matchesShoes = socks.baseColor == shoes.baseColor
            if (!matchesTrousers && !matchesShoes) {
                score -= 1.0f
                feedback.add("Socks do not coordinate with trousers or shoes.")
            } else {
                feedback.add("Socks coordinate well with the outfit.")
            }
        }
        
        return score.coerceIn(0f, 10f)
    }

    private fun calculatePatternScore(
        jacket: Garment,
        shirt: Garment,
        trousers: Garment,
        tie: Garment?,
        feedback: MutableList<String>
    ): Float {
        var score = 10.0f
        
        // Simple pattern clash detection (e.g., multiple large patterns)
        val patterns = listOfNotNull(jacket, shirt, trousers, tie)
        val largePatterns = patterns.filter { it.patternScale >= 3 }
        
        if (largePatterns.size > 1) {
            score -= 3.0f
            feedback.add("Multiple large-scale patterns can be visually overwhelming.")
        } else if (largePatterns.size == 1) {
            feedback.add("Large pattern in ${largePatterns[0].name} is well balanced.")
        }
        
        return score.coerceIn(0f, 10f)
    }

    private fun calculateFormalityScore(
        jacket: Garment,
        shirt: Garment,
        trousers: Garment,
        shoes: Garment,
        tie: Garment?,
        feedback: MutableList<String>
    ): Float {
        val items = listOfNotNull(jacket, shirt, trousers, shoes, tie)
        val avgFormality = items.map { it.formalityLevel }.average().toFloat()
        
        var penalty = 0.0f
        items.forEach { item ->
            val diff = kotlin.math.abs(item.formalityLevel - avgFormality)
            if (diff > 1.5) {
                penalty += 2.0f
                feedback.add("${item.name} is significantly more/less formal than the rest of the outfit.")
            }
        }
        
        if (penalty == 0f) {
            feedback.add("Excellent formality consistency.")
        }
        
        return (10.0f - penalty).coerceIn(0f, 10f)
    }
}
