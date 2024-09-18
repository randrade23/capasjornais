package pt.ruiandrade.capasjornais.models

data class NewspaperCover(
    val newspaper: Newspaper,
    val image: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NewspaperCover

        if (newspaper != other.newspaper) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = newspaper.hashCode()
        result = 31 * result + image.contentHashCode()
        return result
    }
}
