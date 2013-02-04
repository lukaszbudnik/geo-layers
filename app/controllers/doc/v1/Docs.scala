package controllers.doc.v1

import play.api._
import play.api.mvc._

object Docs extends Controller {

  def index = Action {
    Ok(views.html.doc.v1.index())
  }

}