package pt.ruiandrade.capasjornais

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.util.reflect.*
import pt.ruiandrade.capasjornais.models.Feed.Companion.DEFAULT_FEEDS_LIST
import pt.ruiandrade.capasjornais.models.Newspaper.Companion.DEFAULT_NEWSPAPERS_LIST
import pt.ruiandrade.capasjornais.parser.*
import pt.ruiandrade.capasjornais.publisher.SocialMediaPublisher
import pt.ruiandrade.capasjornais.publisher.twitter.TwitterPublisher
import pt.ruiandrade.capasjornais.publisher.twitter.TwitterSetup.getTwitterInstance
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

suspend fun main() {
    val now = LocalDateTime.now().format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    )

    // Create the HttpClient
    val client = HttpClient(CIO)

    // Create instances of the new dependencies
    val httpClientService = HttpClientService(client)
    val xmlParser = XmlParser()
    val imageDownloader = ImageDownloader(client)

    val feeds = DEFAULT_FEEDS_LIST
    val newspapers = DEFAULT_NEWSPAPERS_LIST

    val newspaperCoverExtractor = NewspaperCoverExtractor(
        httpClientService,
        xmlParser,
        imageDownloader,
        feeds
    )

    val twitter = getTwitterInstance()
    val twitterPublisher = TwitterPublisher(twitter)

    val publishers = listOf(twitterPublisher)

    newspapers
        .sortedBy { it.category }
        .forEach { newspaper ->
            val cover = newspaperCoverExtractor.extract(newspaper)
            publishers.forEach { publisher ->
                cover?.let {
                    val handle = newspaper.socialMediaHandles.filter {
                        it.instanceOf(publisher.handleType)
                    }
                    publishToSocialMedia(
                        publisher,
                        "#${newspaper.category} ${newspaper.name} - $handle - $now",
                        cover.image
                    )
                }
            }
        }
}

suspend fun publishToSocialMedia(publisher: SocialMediaPublisher, text: String, imageBytes: ByteArray) {
    publisher.publishPost(text, imageBytes)
}
