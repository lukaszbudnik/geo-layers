package models

import play.api.Play.current
import java.util.Date

import org.bson.types.ObjectId
import com.novus.salat.global._

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._
import com.novus.salat.dao.{ SalatDAO, ModelCompanion }

import se.radley.plugin.salat._

case class Client(@Key("_id") id: ObjectId, email: String, token: String, added: Date, isBlocked: Boolean) {

}

object Client extends ModelCompanion[Client, ObjectId] {
  private val collection = mongoCollection("clients")
  
  val dao = new SalatDAO[Client, ObjectId](collection = collection) {}
  
  def all: Iterable[Client] = findAll().toIterable
  
  def findOneByEmailAndToken(email: String, token: String): Option[Client] = {
    findOne(MongoDBObject("email" -> email, "token" -> token))
  }
  
  def update(client: Client): Unit = {
    update(q = DBObject("_id" -> client.id), t = client, upsert = false, multi = false,  wc = new WriteConcern)
  }
}

