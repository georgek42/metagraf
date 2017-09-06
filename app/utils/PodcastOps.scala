package utils

import scala.language.implicitConversions
import scala.util.{ Try, Success, Failure }
import scala.concurrent.{ Future, ExecutionContext }

import models.{ Podcast }
import adapters.{ ResourceAdapter }

import javax.inject.Inject

class PodcastOps (
    podcast: Podcast
) {
    import PodcastOps._

    private var adapter: ResourceAdapter = null
    private implicit var ex: ExecutionContext = null

    @Inject()
    private def injectAdapters (
        adapter: ResourceAdapter,
        ex: ExecutionContext
    ) = {
        this.adapter = adapter
        this.ex = ex
    }
    
    def download_episode(episodeNum: Int): Future[Array[Byte]] = {
        val episode = Try(podcast.episodes(episodeNum)) match {
            case Success(episode) => episode
            case Failure(e) => throw EpisodeNotFound
        }

        adapter.download(episode.audioUrl).map { resp => resp.bodyAsBytes.toArray }
    }
}

object PodcastOps {
    implicit def toPodcastOps(p: Podcast) = new PodcastOps(p)

    case object EpisodeNotFound extends Throwable
}