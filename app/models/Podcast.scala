package models

import play.api.libs.json._

import adapters.{ ResourceAdapter }
import utils.PodcastOps._

case class Podcast(
    name: String,
    episodes: List[Episode],
    meta: PodcastMeta
)

object Podcast {
    implicit val podcastFormat = Json.format[Podcast]
}