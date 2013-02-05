package security

import play.api.mvc.Request
import play.api.mvc.Result
import play.api.mvc.Results
import models.Client
import play.api.libs.json.Json

trait Security {

  val GeoLayersClientId = "X-GEO-LAYERS-CLIENT-ID"
  val GeoLayersClientToken = "X-GEO-LAYERS-CLIENT-TOKEN"

  def verify(result: () => Result)(implicit request: Request[_]): Result = {

    val client = (for (
      id <- request.headers.get(GeoLayersClientId);
      token <- request.headers.get(GeoLayersClientToken);
      client <- Client.findOneByEmailAndToken(id, token)
    ) yield client)

    client match {
      case Some(Client(_, _, _, _, false)) => result()
      case Some(Client(_, _, _, _, true)) => Results.Forbidden(Json.toJson("The client is blocked"))
      case None => Results.Unauthorized(Json.toJson("The client id and/or client token not found in request"))
    }

  }

}
