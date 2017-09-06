import com.google.inject.AbstractModule

import javax.inject.Inject
import play.api.libs.concurrent.AkkaGuiceSupport
import com.google.inject.name.Names

import adapters.{ ITunesAdapter, ResourceAdapter, RSSAdapter }
import clients.{ ITunesClient, RSSClient }

class Module extends AbstractModule with AkkaGuiceSupport {

    override def configure() = {
  }

}