package pt.ruiandrade.capasjornais.publisher

import pt.ruiandrade.capasjornais.models.handles.SocialMediaHandle
import kotlin.reflect.KClass

interface SocialMediaPublisher {
    val handleType: KClass<*>

    suspend fun publishPost(text: String, imageBytes: ByteArray)
}
