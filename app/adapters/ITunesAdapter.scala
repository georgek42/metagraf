package adapters

import play.api.libs.ws.{ WSClient, WSRequest, WSResponse }

import scala.concurrent.{ Future, ExecutionContext }
import javax.inject.Inject

class ITunesAdapter @Inject() (
    ws: WSClient
) {

    def search(term: String, entity: String): Future[WSResponse] = {
        val url = s"https://itunes.apple.com/search?term=$term&entity=$entity"
        ws.url(url).get
    }
}