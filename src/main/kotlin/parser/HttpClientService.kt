package pt.ruiandrade.capasjornais.parser

import io.ktor.client.*
import io.ktor.client.request.*

class HttpClientService(private val client: HttpClient) {
    suspend fun fetchXmlData(url: String): String {
        return client.get(url).toString()
    }
}
