package models

import play.api.Play.current
import java.util.Date

import org.bson.types.ObjectId
import org.bson.types.BasicBSONList
import com.novus.salat.global._

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._
import org.scala_tools.time.Imports._
import com.novus.salat.dao.{ SalatDAO, ModelCompanion }

import se.radley.plugin.salat._

case class Location(@Key("_id") id: ObjectId, layerType: String, coordinates: BasicBSONList, description: Option[String])

case class Coordinates(latitude: Double, longitude: Double)

object Location extends ModelCompanion[Location, ObjectId] {
  private val collection = mongoCollection("locations")

  val dao = new SalatDAO[Location, ObjectId](collection = collection) {}
  
  def all: Iterable[Location] = {
    findAll().toIterable
  }
  
  def findByLayerType(layerType: String): Iterable[Location] = {
    find(MongoDBObject("layerType" -> layerType)).toIterable
  }
  
  def findByLayerTypeAndCoordinates(layerType: String, coordinates: Coordinates): Iterable[Location] = {
    
    val builder = MongoDBList.newBuilder
    builder += coordinates.latitude
    builder += coordinates.longitude

	val list = builder.result
    
    val nearQuery = MongoDBObject("$near" -> list)
    
    val coordinatesQuery = MongoDBObject("layerType" -> layerType, "coordinates" -> nearQuery)
    
    find(coordinatesQuery).toIterable
  }
  
  def findByLayerTypeAndCoordinatesAndMaxDistance(layerType: String, coordinates: Coordinates, maxDistance: Double): Iterable[Location] = {
    
    val coordinatesBuilder = MongoDBList.newBuilder
    coordinatesBuilder += coordinates.latitude
    coordinatesBuilder += coordinates.longitude

	val coordinatesList = coordinatesBuilder.result
	
	val centerBuilder = MongoDBList.newBuilder
	centerBuilder += coordinatesList
	centerBuilder += maxDistance
	
	val centerList = centerBuilder.result
    
	val centerQuery = MongoDBObject("$center" -> centerList)
	
    val withinQuery = MongoDBObject("$within" -> centerQuery)
    
    val coordinatesQuery = MongoDBObject("layerType" -> layerType, "coordinates" -> withinQuery)
    
    find(coordinatesQuery).toIterable
  }
  
}
