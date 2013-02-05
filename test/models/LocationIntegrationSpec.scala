package models

import java.util.Date
import org.bson.types.ObjectId
import org.specs2.mutable.Specification
import play.api.test.Helpers.running
import play.api.test.FakeApplication
import org.bson.types.BasicBSONList
import com.mongodb.casbah.commons.MongoDBList
import com.mongodb.BasicDBList

/**
 * Requires mongod running
 */
class LocationIntegrationSpec extends Specification {

  "Location model" should {

    "be insertable" in {
      running(FakeApplication()) {

        val list = new BasicBSONList
        list.put(0, 10);
        list.put(1, 10.1)

        val location = Location(new ObjectId, "test", list, None)

        val id = Location.insert(location)

        id must beSome[ObjectId]
      }
    }

    "be retrievable in bulk and by id" in {
      running(FakeApplication()) {
        val all = Location.all

        all.size must be greaterThan (0)

        val head = all.head

        val location = Location.findOneById(head.id)
        
        location must beSome[Location]
      }
    }

    "be retrievable by layerType" in {
      running(FakeApplication()) {
        val locations1 = Location.findByLayerType("test")
        locations1.size must be greaterThan(0)
        
        val locations2 = Location.findByLayerType("test2")
        locations2 must beEmpty
      }
    }
    
    "be retrievable by layerType and coordinates" in {
      running(FakeApplication()) {
        val locations1 = Location.findByLayerTypeAndCoordinates("test", Coordinates(10.3, 10.0))
        locations1.size must be greaterThan(0)
        
        val locations2 = Location.findByLayerTypeAndCoordinates("test2", Coordinates(10.3, 10.0))
        locations2 must beEmpty
      }
    }
    
    "be retrievable by layerType and coordinates" in {
      running(FakeApplication()) {
        val locations1 = Location.findByLayerTypeAndCoordinatesAndMaxDistance("test", Coordinates(10.3, 10.0), 0.0001)
        locations1 must beEmpty
        
        val locations2 = Location.findByLayerTypeAndCoordinatesAndMaxDistance("test", Coordinates(10.3, 10.0), 0.4)
        locations2.size must beGreaterThan(0)
        
        val locations3 = Location.findByLayerTypeAndCoordinatesAndMaxDistance("test2", Coordinates(10.3, 10.0), 0.4)
        locations3 must beEmpty
      }
    }
    
    "be removable" in {
      running(FakeApplication()) {
        val locations1 = Location.findByLayerType("test")

        locations1.foreach(Location.remove(_))

        val locations2 = Location.findByLayerType("test")

        locations2 must beEmpty
      }
    }

  }
}
