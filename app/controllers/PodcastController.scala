package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.ws.{ WSClient, WSRequest, WSResponse }

import clients.{ ITunesClient, RSSClient }
import models.{ Podcast }
import adapters.{ AssetAdapter }
import utils.PodcastOps._

import scala.concurrent.{ Future, ExecutionContext }
import scala.concurrent._
import scala.concurrent.duration._
import scala.language.postfixOps

import javax.inject._


@Singleton
class PodcastController @Inject() (
    iTunes: ITunesClient,
    rss: RSSClient,
    ws: WSClient,
    assetAdapter: AssetAdapter
) (implicit val ex: ExecutionContext) extends InjectedController {

    def lookupPodcast(query: String) = Action.async { request =>
        val podcasts = iTunes.lookupPodcasts(query)
        val ps = Await.result(podcasts, 2 seconds)
        val withEpisodes = iTunes.retrieveEpisodes(ps)
        val we = Await.result(withEpisodes, 2 seconds)

        // Download last podcast
        val audio = Await.result(we.head.using(assetAdapter, ex).downloadEpisode(5), 2 seconds)

        Future(Ok(Json.toJson("ok")))
    }
}