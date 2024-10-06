package pt.ruiandrade.capasjornais.models.handles

data class TwitterSocialMediaHandle(override val handle: String) : SocialMediaHandle {
    override fun toString(): String = "@$handle"
}
