package models

import play.api.libs.json._

case class Podcast(
    name: String,
    episodes: List[Episode],
    meta: PodcastMeta
)

object Podcast {
    implicit val podcastFormat = Json.format[Podcast]
}