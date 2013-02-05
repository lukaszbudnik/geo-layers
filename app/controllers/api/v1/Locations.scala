package controllers.api.v1

import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller
import models.Location
import security.Security
import models.Coordinates

object Locations extends Controller with Security {

  def locationsByLayerType(layerType: String) = Action { implicit request =>
    verify {
      () =>
        {
          val locations = Location.findByLayerType(layerType)
          Ok(Json.parse(Location.toCompactJSONArray(locations)))
        }
    }
  }
  
  def locationsByLayerTypeAndCoordinates(layerType: String, latitude: Double, longitude: Double) = Action { implicit request =>
    verify {
      () =>
        {
          val locations = Location.findByLayerTypeAndCoordinates(layerType, Coordinates(latitude, longitude))
          Ok(Json.parse(Location.toCompactJSONArray(locations)))
        }
    }
  }
  
  def locationsByLayerTypeAndCoordinatesAndMaxDistance(layerType: String, latitude: Double, longitude: Double, maxDistance: Double) = Action { implicit request =>
    verify {
      () =>
        {
          val locations = Location.findByLayerTypeAndCoordinatesAndMaxDistance(layerType, Coordinates(latitude, longitude), maxDistance)
          Ok(Json.parse(Location.toCompactJSONArray(locations)))
        }
    }
  }

}
