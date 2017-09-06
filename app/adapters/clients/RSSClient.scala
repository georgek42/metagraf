package clients

import javax.inject.Inject

import adapters.{ RSSAdapter }
import models.{ Episode }

import scala.concurrent.{ Future, ExecutionContext }
import scala.xml.Node

class RSSClient @Inject() (
    adapter: RSSAdapter
) (implicit val ex: ExecutionContext) {

    def retrieveEpisodes(url: String): Future[List[Episode]] = {
        for {
            rssFeed <- adapter.retrieveFeed(url)
        } yield
            (rssFeed.xml \\ "item")
            .foldLeft(List(): List[Episode])((el: List[Episode], ep: Node) => {

                val title = (ep \ "title").text.toString
                val description = (ep \ "description").text.toString
                val pubDate = (ep \ "pubDate").text.toString
                val duration = (ep \ "duration").text.toString
                val audioUrl = (ep \\ "enclosure").head.attribute("url").get.head.text.toString

                el ++ List(Episode(title, description, pubDate, duration, audioUrl))
            })
    }
}