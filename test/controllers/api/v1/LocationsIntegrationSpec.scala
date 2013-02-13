package controllers.api.v1

import org.specs2.mutable.Specification

import play.api.test.Helpers.GET
import play.api.test.Helpers.OK
import play.api.test.Helpers.contentAsString
import play.api.test.Helpers.contentType
import play.api.test.Helpers.route
import play.api.test.Helpers.running
import play.api.test.Helpers.status
import play.api.test.Helpers.writeableOf_AnyContentAsFormUrlEncoded
import play.api.test.FakeApplication
import play.api.test.FakeRequest
import play.api.test.FakeHeaders
import play.api.mvc.AnyContentAsFormUrlEncoded
import play.api.mvc.Result
import play.api.http.Status
import play.api.libs.json.Json

import security.Security

class LocationsIntegrationSpec extends Specification {

  "Locations API v1" should {

    "return locations by layerType" in {
      running(FakeApplication()) {

        val fakeHeadersMap: Seq[(String, Seq[String])] = Seq(
          Security.GeoLayersClientId -> Seq("lukasz.budnik@hotmail.com"),
          Security.GeoLayersClientToken -> Seq("token"))

        val fakeHeaders = FakeHeaders(fakeHeadersMap)

        val fakeBody = AnyContentAsFormUrlEncoded(Map("limit" -> Seq("10")))

        val fakeRequest = FakeRequest("POST", "/api/v1/locations/integrationTest", fakeHeaders, fakeBody, null)

        val resultOption = route(fakeRequest)

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
