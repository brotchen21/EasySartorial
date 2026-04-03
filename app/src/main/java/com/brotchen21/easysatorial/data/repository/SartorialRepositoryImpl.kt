package com.brotchen21.easysatorial.data.repository

import android.util.Log
import com.brotchen21.easysatorial.core.scoring.ScoringEngine
import com.brotchen21.easysatorial.data.mapper.toDomain
import com.brotchen21.easysatorial.data.remote.SupabaseClientProvider
import com.brotchen21.easysatorial.data.remote.dto.GarmentDto
import com.brotchen21.easysatorial.data.remote.dto.GarmentTypeDto
import com.brotchen21.easysatorial.data.remote.dto.PatternDto
import com.brotchen21.easysatorial.domain.model.*
import com.brotchen21.easysatorial.domain.repository.SartorialRepository
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage

class SartorialRepositoryImpl(private val scoringEngine: ScoringEngine) : SartorialRepository {
    
    private val client = SupabaseClientProvider.client
    private val bucket = client.storage["garments"]

    override suspend fun getGarmentTypes(): List<GarmentType> {
        return try {
            val response = client.postgrest["garment_types"].select().decodeList<GarmentTypeDto>()
            Log.d("SartorialRepo", "Fetched ${response.size} garment types")
            response.map { it.toDomain() }
        } catch (e: Exception) {
            Log.e("SartorialRepo", "Error fetching garment types: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getGarments(typeId: Int): List<Garment> {
        return try {
            val response = client.postgrest["garments"].select {
                filter {
                    eq("garment_type_id", typeId)
                }
            }.decodeList<GarmentDto>()
            
            Log.d("SartorialRepo", "Fetched ${response.size} garments for type $typeId")
            
            response.map { dto ->
                dto.copy(
                    baseUrl = dto.baseUrl?.let { bucket.publicUrl(it) },
                    shadingUrl = dto.shadingUrl?.let { bucket.publicUrl(it) },
                    patternOverlayUrl = dto.patternOverlayUrl?.let { bucket.publicUrl(it) }
                ).toDomain()
            }
        } catch (e: Exception) {
            Log.e("SartorialRepo", "Error fetching garments for type $typeId: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun getPatterns(): List<Pattern> {
        return try {
            client.postgrest["patterns"].select().decodeList<PatternDto>().map { it.toDomain() }
        } catch (e: Exception) {
            Log.e("SartorialRepo", "Error fetching patterns: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun generateOutfit(): Outfit {
        val allGarments = try {
            client.postgrest["garments"].select().decodeList<GarmentDto>().map { dto ->
                dto.copy(
                    baseUrl = dto.baseUrl?.let { bucket.publicUrl(it) },
                    shadingUrl = dto.shadingUrl?.let { bucket.publicUrl(it) },
                    patternOverlayUrl = dto.patternOverlayUrl?.let { bucket.publicUrl(it) }
                ).toDomain()
            }
        } catch (e: Exception) {
            return Outfit(0, 0, 0, 0)
        }

        val jackets = allGarments.filter { it.garmentTypeId == 1 }
        val shirts = allGarments.filter { it.garmentTypeId == 3 }
        val trousers = allGarments.filter { it.garmentTypeId == 4 }
        val ties = allGarments.filter { it.garmentTypeId == 5 }
        val shoes = allGarments.filter { it.garmentTypeId == 7 }

        if (jackets.isEmpty() || shirts.isEmpty() || trousers.isEmpty()) {
            return Outfit(0, 0, 0, 0)
        }

        val jacket = jackets.random()
        val bestTrouser = trousers.maxByOrNull { t -> 
            scoringEngine.calculateScore(jacket, shirts.firstOrNull() ?: jacket, t, null, null, null, null, null, null).score
        } ?: trousers.random()

        val bestShirt = shirts.maxByOrNull { s ->
            scoringEngine.calculateScore(jacket, s, bestTrouser, null, null, null, null, null, null).score
        } ?: shirts.random()

        val bestTie = ties.maxByOrNull { tie ->
            scoringEngine.calculateScore(jacket, bestShirt, bestTrouser, null, null, null, tie, null, null).score
        }

        return Outfit(
            jacketId = jacket.id,
            shirtId = bestShirt.id,
            trousersId = bestTrouser.id,
            shoesId = shoes.firstOrNull()?.id,
            tieId = bestTie?.id
        )
    }

    override suspend fun validateOutfit(outfit: Outfit): OutfitValidationResult {
        val jacket = fetchGarment(outfit.jacketId) ?: return OutfitValidationResult(0f, 0f, 0f, listOf("Missing jacket"))
        val shirt = fetchGarment(outfit.shirtId) ?: return OutfitValidationResult(0f, 0f, 0f, listOf("Missing shirt"))
        val trousers = fetchGarment(outfit.trousersId) ?: return OutfitValidationResult(0f, 0f, 0f, listOf("Missing trousers"))
        val shoes = fetchGarment(outfit.shoesId)
        
        val belt = fetchGarment(outfit.beltId)
        val socks = fetchGarment(outfit.sockId)
        val tie = fetchGarment(outfit.tieId)
        val waistcoat = fetchGarment(outfit.waistcoatId)
        val hat = fetchGarment(outfit.hatId)

        return scoringEngine.calculateScore(
            jacket, shirt, trousers, shoes, belt, socks, tie, waistcoat, hat
        )
    }

    private suspend fun fetchGarment(id: Int?): Garment? {
        if (id == null || id == 0) return null
        return try {
            client.postgrest["garments"].select {
                filter {
                    eq("id", id)
                }
            }.decodeSingleOrNull<GarmentDto>()?.let { dto ->
                dto.copy(
                    baseUrl = dto.baseUrl?.let { bucket.publicUrl(it) },
                    shadingUrl = dto.shadingUrl?.let { bucket.publicUrl(it) },
                    patternOverlayUrl = dto.patternOverlayUrl?.let { bucket.publicUrl(it) }
                ).toDomain()
            }
        } catch (e: Exception) {
            Log.e("SartorialRepo", "Error fetching garment $id: ${e.message}")
            null
        }
    }
}
