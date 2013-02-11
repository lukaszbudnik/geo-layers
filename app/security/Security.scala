package security

import play.api.libs.json.Json
import play.api.mvc.BodyParsers.parse
import play.api.mvc.BodyParser
import play.api.mvc.Request
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.Result
import play.api.mvc.Results
import play.api.mvc.WrappedRequest

import models.Client

case class AuthenticatedRequest[A](client: Client, request: Request[A]) extends WrappedRequest(request)

object Security {
  val GeoLayersClientId = "X-GEO-LAYERS-CLIENT-ID"
  val GeoLayersClientToken = "X-GEO-LAYERS-CLIENT-TOKEN"
}

trait Security {

  def Authenticated[A](p: BodyParser[A])(f: AuthenticatedRequest[A] => Result) = {
    Action(p) { request =>
      val client = (for (
        id <- request.headers.get(Security.GeoLayersClientId);
        token <- request.headers.get(Security.GeoLayersClientToken);
        client <- Client.findOneByEmailAndToken(id, token)
      ) yield client)
      
      client match {
        case None => Results.Unauthorized(Json.toJson("The client id and/or client token not found in request"))
        case Some(Client(_, _, _, _, true)) => Results.Forbidden(Json.toJson("The client is blocked"))
        case Some(client) => f(AuthenticatedRequest(client, request))
      }
    }
  }

  def Authenticated(f: AuthenticatedRequest[AnyContent] => Result): Action[AnyContent] = {
    Authenticated(parse.anyContent)(f)
  }

}
