package pt.ruiandrade.capasjornais.publisher.twitter

import pt.ruiandrade.capasjornais.publisher.SocialMediaPublisher
import twitter4j.StatusUpdate
import twitter4j.Twitter
import java.io.ByteArrayInputStream

class TwitterPublisher(private val twitter: Twitter) : SocialMediaPublisher {
    override suspend fun publishPost(text: String, imageBytes: ByteArray) {
        val status = StatusUpdate(text)
        status.setMedia("image.png", ByteArrayInputStream(imageBytes)) // Set the image to be uploaded
        twitter.updateStatus(status)
    }
}
