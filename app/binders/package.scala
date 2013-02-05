import java.net.URLDecoder
import play.api.mvc.PathBindable

object `package` {

  /**
   * Path binder for Double.
   */
  implicit def doublePathBindable = new PathBindable[Double] {
    def bind(key: String, value: String) = {
      try {
        Right(java.lang.Double.parseDouble(URLDecoder.decode(value, "utf-8")))
      } catch {
        case e: NumberFormatException => Left("Cannot parse parameter " + key + " as Int: " + e.getMessage)
      }
    }
    def unbind(key: String, value: Double) = value.toString
  }
  
}