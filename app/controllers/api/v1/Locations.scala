package controllers.api.v1

import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller
import models.Location
import security.Security
import models.Coordinates
import security.SecuredAction

object Locations extends Controller {

  def locationsByLayerType(layerType: String) = SecuredAction { implicit request =>
    val locations = Location.findByLayerType(layerType)
    Ok(Json.parse(Location.toCompactJSONArray(locations)))
  }

  def locationsByLayerTypeAndCoordinates(layerType: String, latitude: Double, longitude: Double) = SecuredAction { implicit request =>
    val locations = Location.findByLayerTypeAndCoordinates(layerType, Coordinates(latitude, longitude))
    Ok(Json.parse(Location.toCompactJSONArray(locations)))
  }

  def locationsByLayerTypeAndCoordinatesAndMaxDistance(layerType: String, latitude: Double, longitude: Double, maxDistance: Double) = SecuredAction { implicit request =>
    val locations = Location.findByLayerTypeAndCoordinatesAndMaxDistance(layerType, Coordinates(latitude, longitude), maxDistance)
    Ok(Json.parse(Location.toCompactJSONArray(locations)))
  }

}
