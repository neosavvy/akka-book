package com.neosavvy.avionics

import akka.actor.{Props, ActorSystem, Actor}
import akka.testkit.{TestActorRef, TestKit, ImplicitSender}
import org.scalatest.{WordSpecLike, Matchers, BeforeAndAfterAll}

/**
 * Created by aparrish on 9/3/14.
 */
class TestEventSource extends Actor with ProductionEventSource {

  def receive = eventSourceReceive

}

class EventSourceSpec extends TestKit(ActorSystem("EventSourceSpec"))
          with WordSpecLike
          with Matchers
          with BeforeAndAfterAll {

  import EventSource._
  override def afterAll() { system.shutdown() }
  "EventSource" should {
    "allow us to register a listener" in {
      val real = TestActorRef[TestEventSource].underlyingActor
      real.receive(RegisterListener(testActor))
      real.receive(UnregisterListener(testActor))
      real.listeners.size should be (0)
    }
    "send the event to our test actor" in {
      val testA = TestActorRef[TestEventSource]
      testA ! RegisterListener(testActor)
      testA.underlyingActor.sendEvent("Fibonacci")
      expectMsg("Fibonacci")
    }
  }
}
