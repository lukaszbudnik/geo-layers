package models

import java.util.Date

import org.bson.types.ObjectId
import org.specs2.mutable.Specification

import play.api.test.Helpers.running
import play.api.test.FakeApplication

/**
 * Requires mongod running
 */
class ClientIntegrationSpec extends Specification {

  "Client model" should {

    "be insertable" in {
      running(FakeApplication()) {
        val client = Client(new ObjectId, "email", "token", new Date, true)

        val id = Client.insert(client)

        id must beSome[ObjectId]
      }
    }

    "be retrievable in bulk and by id" in {
      running(FakeApplication()) {
        val all = Client.all

        all.size must be greaterThan(0)

        val head = all.head

        val client = Client.findOneById(head.id)

        client must beSome[Client]
      }
    }

    "be retrievable by email and token" in {
      running(FakeApplication()) {
        val client = Client.findOneByEmailAndToken("email", "token")
        client must beSome[Client]
      }
    }

    "be updatable" in {
      running(FakeApplication()) {
        val client = Client.findOneByEmailAndToken("email", "token").get

        Client.update(client.copy(email = "email2", token = "token2", isBlocked = false))

        val updatedClient = Client.findOneById(client.id)

        updatedClient must beSome.which {
          _ match {
            case (Client(_, "email2", "token2", _, false)) => true
            case _ => false
          }
        }
      }
    }

    "be removable" in {
      running(FakeApplication()) {
        val client = Client.findOneByEmailAndToken("email2", "token2")

        Client.remove(client.get)

        val removedClient = Client.findOneById(client.get.id)

        removedClient must beNone
      }
    }
  }
}
