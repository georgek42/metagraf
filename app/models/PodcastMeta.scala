package models

import play.api.libs.json._

case class PodcastMeta(
    artworkUrl: String,
    feedUrl: String
)

object PodcastMeta {
    implicit val podcastMetaFormat = Json.format[PodcastMeta]
}