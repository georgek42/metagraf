package utils

import scala.language.implicitConversions
import scala.util.{ Try, Success, Failure }
import scala.concurrent.{ Future, ExecutionContext }

import models.{ Podcast, Episode }
import adapters.{ AssetAdapter }

class InjectedPodcastOps (
    podcast: Podcast,
    adapter: AssetAdapter,
    implicit var ex: ExecutionContext
) {
    import PodcastOps._

    def downloadEpisode(episodeNum: Int): Future[Array[Byte]] = {
        val episode: Episode = Try(podcast.episodes(episodeNum)) match {
            case Success(episode) => episode
            case Failure(e) => throw EpisodeNotFound
        }

        adapter.download(episode.audioUrl) map { resp => resp.bodyAsBytes.toArray }
    }
}

class PodcastOps (
    podcast: Podcast
) {
    import PodcastOps._

    def using(
        adapter: AssetAdapter,
        ex: ExecutionContext
    ): InjectedPodcastOps = {
        new InjectedPodcastOps(podcast, adapter, ex)
    }

    def findEpisodes (
        title: Option[String] = None,
        description: Option[String] = None,
        pubDate: Option[String] = None,
        duration: Option[String] = None,
        audioUrl: Option[String] = None): List[Episode] = {
            podcast.episodes.filter { ep =>
                (title.isEmpty || (title.isDefined && ep.title == title.get)) &&
                (description.isEmpty || (description.isDefined && ep.description == description.get)) &&
                (pubDate.isEmpty || (pubDate.isDefined && ep.pubDate == pubDate.get)) &&
                (duration.isEmpty || (duration.isDefined && ep.duration == duration.get)) &&
                (audioUrl.isEmpty || (audioUrl.isDefined && ep.audioUrl == audioUrl.get))
            }
        }
}

object PodcastOps {
    implicit def toPodcastOps(p: Podcast) = new PodcastOps(p)

    case object EpisodeNotFound extends Throwable
}