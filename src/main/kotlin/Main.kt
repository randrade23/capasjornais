package pt.ruiandrade.capasjornais

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import pt.ruiandrade.capasjornais.parser.NewsFeedParser
import pt.ruiandrade.capasjornais.publisher.SocialMediaPublisher
import pt.ruiandrade.capasjornais.publisher.twitter.TwitterPublisher
import pt.ruiandrade.capasjornais.publisher.twitter.TwitterSetup.getTwitterInstance
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val newspapers = listOf(
    "Expresso",
    "Nascer do SOL",
    "Correio da Manhã",
    "Jornal de Notícias",
    "Público",
    "Diário de Notícias",
    "i",
    "O Jornal Económico",
    "Jornal de Negócios",
    "O Jogo",
    "A Bola",
    "Record"
)

suspend fun main() {
    val now = LocalDateTime.now().format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    )
    val client = HttpClient(CIO)
    val parser = NewsFeedParser(client, newspapers)

    val twitter = getTwitterInstance()
    val twitterPublisher = TwitterPublisher(twitter)

    val publishers = listOf(twitterPublisher)

    val feeds = mapOf(
        "Sport" to "https://services.sapo.pt/News/NewsStand/Sport",
        "National" to "https://services.sapo.pt/News/NewsStand/National",
        "Economy" to "https://services.sapo.pt/News/NewsStand/Economy"
    )

    feeds.forEach { (category, url) ->
        val covers = parser.getNewspaperCovers(url)
        covers.forEach { (name, image) ->
            publishers.forEach { publisher ->
                publishToSocialMedia(publisher, "#$category $name - $now", image)
            }
        }
    }
}

suspend fun publishToSocialMedia(publisher: SocialMediaPublisher, text: String, imageBytes: ByteArray) {
    publisher.publishPost(text, imageBytes)
}
