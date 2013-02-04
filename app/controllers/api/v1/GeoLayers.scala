package controllers.api.v1

import play.api.mvc.Action
import play.api.mvc.Controller
import security.Security

object GeoLayers extends Controller {

  def layers(layerType: String) = Action { implicit request =>
    Security.verify {
      Ok(layerType)
    }
  }

}
