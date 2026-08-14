package com.financeapp.data.service

import com.financeapp.BuildConfig
import com.financeapp.data.remote.GeminiApiService
import com.financeapp.data.remote.GeminiRequest
import com.financeapp.domain.model.ReceiptData
import com.financeapp.domain.model.TransactionCategory
import com.financeapp.domain.repository.ReceiptParserRepository
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GeminiReceiptParser @Inject constructor(
    private val geminiApiService: GeminiApiService
) : ReceiptParserRepository {

    override suspend fun parseReceiptText(rawText: String): Result<ReceiptData> {
        return try {
            val response = geminiApiService.createInteraction(
                apiKey  = BuildConfig.GEMINI_API_KEY,
                request = GeminiRequest(
                    model = "gemini-3.6-flash",
                    input = buildPrompt(rawText)
                )
            )

            // Ambil teks dari output_text atau dari steps
            val responseText = response.steps
                ?.firstOrNull { it.type == "model_output" }
                ?.content
                ?.firstOrNull { it.type == "text" }
                ?.text
                ?: return Result.failure(Exception("Gemini tidak memberikan respons"))

            val cleanJson = responseText
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            Result.success(parseJsonToReceiptData(cleanJson, rawText))

        } catch (e: Exception) {
            Result.failure(Exception("Gagal menganalisis struk: ${e.message}"))
        }
    }

    private fun buildPrompt(rawText: String): String = """
        Kamu adalah parser struk belanja Indonesia.
        Dari teks struk berikut, ekstrak informasi dalam format JSON:
        {
          "total": <angka saja tanpa titik/koma/simbol, atau null>,
          "tanggal": <format YYYY-MM-DD atau null>,
          "nama_toko": <nama toko sebagai string, atau null>,
          "kategori": <pilih SATU: FOOD, TRANSPORT, SHOPPING, HEALTH, ENTERTAINMENT, EDUCATION, BILLS, OTHER>
        }
        Hanya balas dengan JSON, tanpa penjelasan apapun.
        Teks struk:
        $rawText
    """.trimIndent()

    private fun parseJsonToReceiptData(json: String, rawText: String): ReceiptData {
        return try {
            val obj = JSONObject(json)
            val total = if (obj.isNull("total")) null
            else obj.optDouble("total").takeIf { it > 0 }
            val date = obj.optString("tanggal", "")
                .takeIf { it.isNotBlank() && it != "null" }
                ?.let {
                    runCatching {
                        LocalDateTime.parse("${it}T00:00:00",
                            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                    }.getOrNull()
                }
            val merchant = obj.optString("nama_toko", "")
                .takeIf { it.isNotBlank() && it != "null" }
            val category = runCatching {
                TransactionCategory.valueOf(obj.optString("kategori", "OTHER"))
            }.getOrDefault(TransactionCategory.OTHER)

            ReceiptData(rawText = rawText, totalAmount = total,
                date = date, merchantName = merchant, category = category)
        } catch (e: Exception) {
            ReceiptData(rawText = rawText, totalAmount = null, date = null, merchantName = null)
        }
    }
}