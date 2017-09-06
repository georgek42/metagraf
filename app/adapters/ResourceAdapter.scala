package adapters

import play.api.libs.ws.{ WSClient, WSRequest, WSResponse }

import scala.concurrent.{ Future, ExecutionContext }
import scala.language.postfixOps
import javax.inject.Inject

import java.io.{ BufferedInputStream, BufferedOutputStream, FileInputStream, FileOutputStream }

class ResourceAdapter @Inject() (
    ws: WSClient
) (implicit val ex: ExecutionContext) {

    def download(url: String): Future[WSResponse] = {
        ws.url(url).get
    }

    def writeToFile(path: String, data: Array[Byte]): Future[Unit] = {
        Future {
            val out = new BufferedOutputStream(new FileOutputStream(path))
            data.map { v => out.write(v) }
            out.close
        }
    }

    def readFromFile(path: String): Future[Array[Byte]] = {
        Future {
            val in = new BufferedInputStream(new FileInputStream(path))
            val data = Stream.continually(in.read).takeWhile(-1 !=).map(_.toByte).toArray
            in.close
            data
        }
    }

}