package pt.ruiandrade.capasjornais.parser

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class ImageDownloader(private val client: HttpClient) {
    suspend fun downloadImage(imageUrl: String): ByteArray {
        val response: HttpResponse = client.get(imageUrl)
        return response.readBytes() // Reads the response content as a ByteArray
    }
}