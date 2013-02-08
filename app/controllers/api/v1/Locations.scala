package controllers.api.v1

import play.api.libs.json.Json
import play.api.mvc.Controller
import models.Coordinates
import models.Location
import security.Security
import play.api.mvc.Request
import play.api.mvc.AnyContent

object Locations extends Controller with Security {

  val maxLimit = 100

  val minLimit = 1
  
  def locationsByLayerType(layerType: String) = Authenticated { implicit request =>
    val locations = Location.findByLayerType(layerType, limit)
    Ok(Json.parse(Location.toCompactJSONArray(locations)))
  }

  def locationsByLayerTypeAndCoordinates(layerType: String, latitude: Double, longitude: Double) = Authenticated { implicit request =>
    val locations = Location.findByLayerTypeAndCoordinates(layerType, Coordinates(latitude, longitude))
    Ok(Json.parse(Location.toCompactJSONArray(locations)))
  }

  def locationsByLayerTypeAndCoordinatesAndMaxDistance(layerType: String, latitude: Double, longitude: Double, maxDistance: Double) = Authenticated { implicit request =>
    val locations = Location.findByLayerTypeAndCoordinatesAndMaxDistance(layerType, Coordinates(latitude, longitude), maxDistance)
    Ok(Json.parse(Location.toCompactJSONArray(locations)))
  }

  private def limit(implicit request: Request[AnyContent]): Int = {
    val limit = for {
      map <- request.body.asFormUrlEncoded;
      limits <- map.get("limit");
      limit <- limits.headOption.filter(_.matches("\\d+")).map(Integer.parseInt(_))
    } yield limit 
    
    limit match {
      case Some(limit) if limit > maxLimit => maxLimit
      case Some(limit) if limit < minLimit => minLimit
      case Some(limit) => limit
      case _ => maxLimit
    }
  }

}
