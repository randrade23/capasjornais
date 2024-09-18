package pt.ruiandrade.capasjornais.models

import pt.ruiandrade.capasjornais.models.handles.SocialMediaHandle
import pt.ruiandrade.capasjornais.models.handles.TwitterSocialMediaHandle

data class Newspaper(
    val name: String,
    val category: Category,
    val socialMediaHandles: List<SocialMediaHandle>
) {
    companion object {
        val DEFAULT_NEWSPAPERS_LIST = listOf(
            Newspaper(
                name = "Expresso",
                category = Category.NATIONAL,
                socialMediaHandles = listOf(
                    TwitterSocialMediaHandle(
                        handle = "@expresso"
                    )
                )
            ),
            Newspaper(
                name = "Nascer do SOL",
                category = Category.NATIONAL,
                socialMediaHandles = listOf(
                    TwitterSocialMediaHandle(
                        handle = "@solonline"
                    )
                )
            ),
            Newspaper(
                name = "Correio da Manhã",
                category = Category.NATIONAL,
                socialMediaHandles = listOf(
                    TwitterSocialMediaHandle(
                        handle = "@cmjornal"
                    )
                )
            ),
            Newspaper(
                name = "Jornal de Notícias",
                category = Category.NATIONAL,
                socialMediaHandles = listOf(
                    TwitterSocialMediaHandle(
                        handle = "@jornalnoticias"
                    )
                )
            ),
            Newspaper(
                name = "Público",
                category = Category.NATIONAL,
                socialMediaHandles = listOf(
                    TwitterSocialMediaHandle(
                        handle = "@publico"
                    )
                )
            ),
            Newspaper(
                name = "Diário de Notícias",
                category = Category.NATIONAL,
                socialMediaHandles = listOf(
                    TwitterSocialMediaHandle(
                        handle = "@dntwit"
                    )
                )
            ),
            Newspaper(
                name = "i",
                category = Category.NATIONAL,
                socialMediaHandles = listOf(
                    TwitterSocialMediaHandle(
                        handle = "@itwitting"
                    )
                )
            ),
            Newspaper(
                name = "Jornal Económico",
                category = Category.ECONOMY,
                socialMediaHandles = listOf(
                    TwitterSocialMediaHandle(
                        handle = "@ojeconomico"
                    )
                )
            ),
            Newspaper(
                name = "Jornal de Negócios",
                category = Category.ECONOMY,
                socialMediaHandles = listOf(
                    TwitterSocialMediaHandle(
                        handle = "@JNegocios"
                    )
                )
            ),
            Newspaper(
                name = "O Jogo",
                category = Category.SPORT,
                socialMediaHandles = listOf(
                    TwitterSocialMediaHandle(
                        handle = "@ojogo"
                    )
                )
            ),
            Newspaper(
                name = "A Bola",
                category = Category.SPORT,
                socialMediaHandles = listOf(
                    TwitterSocialMediaHandle(
                        handle = "@abolapt"
                    )
                )
            ),
            Newspaper(
                name = "Record",
                category = Category.SPORT,
                socialMediaHandles = listOf(
                    TwitterSocialMediaHandle(
                        handle = "@Record_Portugal"
                    )
                )
            )
        )
    }
}
