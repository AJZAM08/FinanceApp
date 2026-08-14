package com.financeapp.data.remote

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class GeminiRequest(
    val model: String,
    val input: String
)

data class GeminiResponse(
    val steps: List<GeminiStep>?
)

data class GeminiStep(
    val type: String?,
    val content: List<GeminiContent>?
)

data class GeminiContent(
    val text: String?,
    val type: String?
)

interface GeminiApiService {
    @POST("v1beta/interactions")
    suspend fun createInteraction(
        @Header("x-goog-api-key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}