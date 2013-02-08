package controllers.doc.v1

import org.specs2.mutable.Specification

import play.api.test.Helpers.GET
import play.api.test.Helpers.OK
import play.api.test.Helpers.contentAsString
import play.api.test.Helpers.contentType
import play.api.test.Helpers.routeAndCall
import play.api.test.Helpers.running
import play.api.test.Helpers.status
import play.api.test.FakeApplication
import play.api.test.FakeRequest

class DocsSpec extends Specification {

  "Docs v1" should {

    "render the index page" in {
      running(FakeApplication()) {
        val home = routeAndCall(FakeRequest(GET, "/")).get

        status(home) must equalTo(OK)
        contentType(home) must beSome.which(_ == "text/html")
        contentAsString(home) must contain("Geo Layers v1")
      }
    }
  }
}