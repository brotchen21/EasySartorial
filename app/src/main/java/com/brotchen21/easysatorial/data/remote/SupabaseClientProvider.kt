package com.brotchen21.easysatorial.data.remote

import com.brotchen21.easysatorial.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.ktor.client.plugins.HttpTimeout
import kotlin.time.Duration.Companion.seconds

object SupabaseClientProvider {
    @OptIn(SupabaseInternal::class)
    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY
        ) {
            install(Postgrest)
            install(Storage)
            install(Functions)
            
            // Increase timeout to 30 seconds to prevent "Request timeout has expired"
            httpConfig {
                install(HttpTimeout) {
                    requestTimeoutMillis = 30.seconds.inWholeMilliseconds
                    connectTimeoutMillis = 30.seconds.inWholeMilliseconds
                    socketTimeoutMillis = 30.seconds.inWholeMilliseconds
                }
            }
        }
    }
}
