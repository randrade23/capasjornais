package pt.ruiandrade.capasjornais.publisher.twitter

import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder

object TwitterSetup {
    fun getTwitterInstance(): twitter4j.Twitter {
        val cb = ConfigurationBuilder()
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey(System.getenv("TWITTER_CONSUMER_KEY"))
            .setOAuthConsumerSecret(System.getenv("TWITTER_CONSUMER_SECRET"))
            .setOAuthAccessToken(System.getenv("TWITTER_ACCESS_TOKEN"))
            .setOAuthAccessTokenSecret(System.getenv("TWITTER_ACCESS_TOKEN_SECRET"))
        return TwitterFactory(cb.build()).instance
    }
}