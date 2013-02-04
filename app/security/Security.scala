package security

import play.api.mvc.Request
import play.api.mvc.AnyContent
import models.Caller
import play.api.mvc.Action
import play.api.mvc.Result
import play.api.mvc.Results

object Security {

  val GeoLayersCallerId = "X-GEOLAYERS-CALLER-ID"
  val GeoLayersCallerToken = "X-GEOLAYERS-CALLER-TOKEN"

  def verify(result: Result)(implicit request: Request[_]): Result = {

    val caller = (for (
      id <- request.headers.get(GeoLayersCallerId);
      token <- request.headers.get(GeoLayersCallerToken)
    ) yield Caller("id", "token", true))

    caller match {
      case Some(Caller(_, _, true)) => result
      case Some(Caller(_, _, false)) => Results.Forbidden("The caller is blocked")
      case None => Results.Forbidden("The caller id and/or caller token not found in request")
    }

  }

}