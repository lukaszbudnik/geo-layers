package controllers.api.v1

import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller
import models.Location
import security.Security
import models.Coordinates
import security.SecuredAction

object Locations extends Controller {

  val maxLimit = 100

  def locationsByLayerType(layerType: String, limit: Option[Int]) = SecuredAction { implicit request =>
    val newLimit = getNewLimit(limit)
    
    val locations = Location.findByLayerType(layerType, Some(newLimit))
    Ok(Json.parse(Location.toCompactJSONArray(locations)))
  }

  def locationsByLayerTypeAndCoordinates(layerType: String, latitude: Double, longitude: Double, limit: Option[Int]) = SecuredAction { implicit request =>
    val newLimit = getNewLimit(limit)

    val locations = Location.findByLayerTypeAndCoordinates(layerType, Coordinates(latitude, longitude))
    Ok(Json.parse(Location.toCompactJSONArray(locations)))
  }

  def locationsByLayerTypeAndCoordinatesAndMaxDistance(layerType: String, latitude: Double, longitude: Double, maxDistance: Double, limit: Option[Int]) = SecuredAction { implicit request =>
    val newLimit = getNewLimit(limit)

    val locations = Location.findByLayerTypeAndCoordinatesAndMaxDistance(layerType, Coordinates(latitude, longitude), maxDistance)
    Ok(Json.parse(Location.toCompactJSONArray(locations)))
  }

  private def getNewLimit(limit: Option[Int]): Int = {
    limit match {
      case Some(limit) if limit <= maxLimit => limit
      case _ => maxLimit
    }
  }

}
