package controllers

import play.api._
import play.api.mvc._

import javax.inject._
import play.api.libs.json._
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class AudioController @Inject()(
    
) (implicit val ex: ExecutionContext) extends InjectedController {

    def transcribe() = Action.async(parse.json) { request =>
        val json = request.body
        println("Hello")
        Future(Ok("ok"))
    }

}