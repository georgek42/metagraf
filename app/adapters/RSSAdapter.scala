package adapters

import play.api.libs.ws.{ WSClient, WSRequest, WSResponse }

import javax.inject.Inject

import scala.concurrent.Future

class RSSAdapter @Inject() (
    ws: WSClient
) {

    def retrieveFeed(url: String): Future[WSResponse] = {
        ws.url(url).get
    }
}