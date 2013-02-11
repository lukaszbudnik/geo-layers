package controllers.api.v1

import java.util.concurrent.TimeUnit
import org.specs2.mutable.Specification
import play.api.libs.json.JsArray
import play.api.libs.ws.WS
import play.api.test.Helpers.running
import play.api.test.FakeApplication
import play.api.test.FakeHeaders
import play.api.test.FakeRequest
import play.api.test.Helpers._
import security.Security
import play.api.mvc.Result
import play.api.mvc.SimpleResult
import play.api.mvc._
import play.api.http.Status
import play.api.libs.json.Json

class LocationsIntegrationSpec extends Specification {

  "Locations API v1" should {

    "return locations by layerType" in {
      running(FakeApplication()) {

        val fakeHeadersMap: Map[String, Seq[String]] = Map(
          Security.GeoLayersClientId -> Seq("lukasz.budnik@hotmail.com"),
          Security.GeoLayersClientToken -> Seq("token"))

        val fakeHeaders = FakeHeaders(fakeHeadersMap)

        val fakeBody = AnyContentAsFormUrlEncoded(Map("limit" -> Seq("10")))

        val fakeRequest = FakeRequest("POST", "/api/v1/locations/integrationTest", fakeHeaders, fakeBody, null)

        val resultOption = routeAndCall(fakeRequest)

        resultOption must beSome[Result].which {
          case result if status(result) == Status.OK => true
          case _ => false
        }

        val result = resultOption.get

        contentType(result) must beSome[String].which {
          case "application/json" => true
          case _ => false
        }

        val contentAsJson = Json.parse(contentAsString(result))

        val layerTypes = (contentAsJson \\ "layerType").map(_.as[String])

        layerTypes.size must beEqualTo(4)

        layerTypes.forall(_.toString() == "integrationTest") must beTrue
      }
    }
  }
}