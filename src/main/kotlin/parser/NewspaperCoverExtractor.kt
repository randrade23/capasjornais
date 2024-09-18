package pt.ruiandrade.capasjornais.parser

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.w3c.dom.Document
import pt.ruiandrade.capasjornais.models.Feed
import pt.ruiandrade.capasjornais.models.Newspaper
import pt.ruiandrade.capasjornais.models.NewspaperCover

class NewspaperCoverExtractor(
    private val httpClientService: HttpClientService,
    private val xmlParser: XmlParser,
    private val imageDownloader: ImageDownloader,
    private val feeds: List<Feed>
) {
    private val namespaceUri = "http://services.sapo.pt/Metadata/News"
    private val cachedXmlDoc: MutableMap<Feed, Document> = mutableMapOf()

    suspend fun extract(newspaper: Newspaper): NewspaperCover? = coroutineScope {
        val feed = feeds.first { it.category == newspaper.category }
        val doc = cachedXmlDoc.getOrPut(feed) {
            xmlParser.parse(
                httpClientService.fetchXmlData(feed.url)
            )
        }
        val newspapers = xmlParser.extractNewspapers(doc, namespaceUri)
        val newspaperEntries = newspapers.map { entry ->
            async {
                val name = entry.getElementsByTagNameNS(namespaceUri, "name").item(0).textContent
                val imageUrl = entry.getElementsByTagNameNS(namespaceUri, "image_url").item(0).textContent
                when {
                    newspaper.name == name -> newspaper to imageDownloader.downloadImage(imageUrl)
                    else -> null
                }
            }.await()
        }

        val newspaperEntry = newspaperEntries.firstOrNull()
        return@coroutineScope newspaperEntry?.let {
            NewspaperCover(newspaper, it.second)
        }
    }
}