
import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import security.Security
import play.api.mvc.Action
import play.api.mvc.AnyContent

class GlobalSpec extends Specification {

  "Global" should {

    "send 403 forbidden when caller data not passed in HTTP headers" in {
      running(FakeApplication()) {
        val request = FakeRequest(GET, "/api/v1/layers/test")

        val handler = Global.onRouteRequest(request).get.asInstanceOf[Action[AnyContent]]

        val result = handler.apply(request)

        status(result) must equalTo(FORBIDDEN)
      }
    }

    "render layers when caller successfully verified" in {
      running(FakeApplication()) {

        val layerType = "qwq"

        val headersMap = Map((Security.GeoLayersCallerId -> Seq("caller id")), (Security.GeoLayersCallerToken -> Seq("token")))

        val headers = FakeHeaders(headersMap)

        val request = FakeRequest(GET, "/api/v1/layers/" + layerType, headers, null, null)

        val handler = Global.onRouteRequest(request).get.asInstanceOf[Action[AnyContent]]

        val result = handler.apply(request)

        status(result) must equalTo(OK)
        contentType(result) must beSome.which(_ == "text/plain")
        contentAsString(result) must contain(layerType)
      }
    }
  }
}
