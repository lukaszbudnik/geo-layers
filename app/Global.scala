import play.api.mvc.Action
import play.api.mvc.Handler
import play.api.mvc.RequestHeader
import play.api.GlobalSettings

import security.Security

object Global extends GlobalSettings {

  //  override def onRouteRequest(requestHeader: RequestHeader): Option[Handler] = {
  //
  //    super.onRouteRequest(requestHeader) map {
  //      case a: Action[_] if requestHeader.path.contains("/api/") => Action(a.parser) { implicit request =>
  //        Security.verify {
  //          a(request)
  //        }
  //      }
  //      case h => h
  //    }
  //  }

}