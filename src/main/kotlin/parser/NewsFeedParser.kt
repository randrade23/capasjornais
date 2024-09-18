package pt.ruiandrade.capasjornais.parser

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.w3c.dom.Element
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class NewsFeedParser(private val client: HttpClient, private val targetNewspapers: List<String>) {
    private val namespaceUri = "http://services.sapo.pt/Metadata/News"

    suspend fun getNewspaperCovers(feedUrl: String): List<Pair<String, ByteArray>> = coroutineScope {
        val xmlData = client.get(URL(feedUrl))
        val dbFactory = DocumentBuilderFactory.newInstance()
        val dBuilder = dbFactory.newDocumentBuilder()
        val doc = withContext(Dispatchers.IO) {
            dBuilder.parse(xmlData.toString())
        }
        val newspapers = doc.getElementsByTagNameNS(namespaceUri, "newspaper")
        List(newspapers.length) { i ->
            async {
                val newspaper = newspapers.item(i) as Element
                val name = newspaper.getElementsByTagNameNS(namespaceUri, "name").item(0).textContent
                val imageUrl = newspaper.getElementsByTagNameNS(namespaceUri, "image_url").item(0).textContent
                name to downloadImage(imageUrl)
            }
        }
    }.mapNotNull { it.await().takeIf { (name, image) -> name in targetNewspapers && image.isNotEmpty() } }


    private suspend fun downloadImage(imageUrl: String): ByteArray {
        val client = HttpClient(CIO)
        return try {
            val response: HttpResponse = client.get(imageUrl)
            if (response.status.value == 200) {
                response.readBytes()
            } else {
                ByteArray(0) // or handle the error as appropriate
            }
        } catch (e: Exception) {
            println("An error occurred while downloading the image: ${e.message}")
            ByteArray(0) // or rethrow the exception after logging it
        } finally {
            client.close()
        }
    }
}
