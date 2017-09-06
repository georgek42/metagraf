package clients

import play.api.libs.json._

import adapters.{ ITunesAdapter }
import models.{ Podcast, PodcastMeta, Episode }

import javax.inject.Inject
import scala.concurrent.{ Future, ExecutionContext }
import scala.util.{ Success, Failure }

class ITunesClient @Inject() (
    iTunes: ITunesAdapter,
    rss: RSSClient
) (implicit val ex: ExecutionContext) {
    import ITunesClient._


    def lookupPodcasts(query: String): Future[List[Podcast]] = {
        for {
            searchResponse <- iTunes.search(query, "podcast")
        } yield
            (searchResponse.json.as[JsValue] \ "results")
            .as[List[JsValue]]
            .foldLeft(List(): List[Podcast])((pl: List[Podcast], js: JsValue) => {

                val name = (js \ "trackName").as[String]
                val artworkUrl = (js \ "artworkUrl600").as[String]
                val feedUrl = (js \ "feedUrl").as[String]

                pl ++ List(Podcast(name, List(), PodcastMeta(artworkUrl, feedUrl)))
            })
    }

    def retrieveEpisodes(podcasts: List[Podcast]): Future[List[Podcast]] = {
        Future.sequence(
            podcasts.foldLeft(List(): List[Future[Podcast]])((pl: List[Future[Podcast]], podcast: Podcast) => {
                val augmentedPodcast: Future[Podcast] = {
                    for {
                        episodes <- rss.retrieveEpisodes(podcast.meta.feedUrl)
                    } yield podcast.copy(episodes = episodes)
                }
                pl ++ List(augmentedPodcast)
            })
        )
    }
}

object ITunesClient {
    case object ParseFeedError extends Throwable
}