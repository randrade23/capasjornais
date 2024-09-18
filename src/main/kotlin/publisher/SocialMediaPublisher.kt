package pt.ruiandrade.capasjornais.publisher

interface SocialMediaPublisher {
    suspend fun publishPost(text: String, imageBytes: ByteArray)
}
