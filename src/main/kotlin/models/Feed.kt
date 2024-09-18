package pt.ruiandrade.capasjornais.models

data class Feed(
    val url: String,
    val category: Category
) {
    companion object {
        val DEFAULT_FEEDS_LIST = listOf(
            Feed(
                url = "https://services.sapo.pt/News/NewsStand/Sport",
                category = Category.SPORT
            ),
            Feed(
                url = "https://services.sapo.pt/News/NewsStand/National",
                category = Category.NATIONAL
            ),
            Feed(
                url = "https://services.sapo.pt/News/NewsStand/Economy",
                category = Category.ECONOMY
            )
        )
    }
}
