package models

import play.api.libs.json._

case class Episode(
    title: String,
    description: String,
    pubDate: String,
    duration: String,
    audioUrl: String    
)

object Episode {
    implicit val episodeFormat = Json.format[Episode]
}