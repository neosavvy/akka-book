package com.neosavvy.avionics

import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{Actor, Props, ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import com.neosavvy.avionics.MyActor.{Ping, Pong}
import org.scalatest._
import org.scalatest.fixture.{ NoArg, UnitFixture }


object ActorSys {
  val uniqueId = new AtomicInteger(0)
}
//class ActorSys(name: String) extends
//TestKit(ActorSystem(name))
//  with ImplicitSender
//with DelayedInit
//with (() => Unit) {
//
//  private var theBody: () => Unit = _
//
//  def this() = this(
//    "TestSystem%05d".format(
//    ActorSys.uniqueId.getAndIncrement()))
//
//  def shutdown():Unit = system.shutdown()
//
//  final def delayedInit(body: => Unit) {
//    theBody = (() => body)
//  }
//
//  override def apply(): Unit = {
//    try theBody()
//    finally shutdown()
//  }
//
//}

class ActorSys(name: String) extends
TestKit(ActorSystem(name))
with ImplicitSender
with fixture.NoArg {

  def this() = this(
    "TestSystem%05d".format(
      ActorSys.uniqueId.getAndIncrement()))

  def shutdown():Unit = system.shutdown()

  override def apply(): Unit = {
    try super.apply()
    finally shutdown()
  }

}

//class TestKitSpec( actorSystem : ActorSystem) extends TestKit(actorSystem)
//  with WordSpecLike with Matchers with BeforeAndAfterAll

object MyActor {
  case class Ping()
  case class Pong()
}


class MyActor extends Actor {

  def receive : Receive = {
    case Ping => {
      println("Receiving a ping....")
      sender ! Pong
    }
  }

}


class MyActorSpec extends fixture.WordSpec
with Matchers
with ParallelTestExecution
with UnitFixture {

  class SubActorSys(name : String) extends ActorSys(name) {
    def makeActor(): ActorRef = system.actorOf(Props(new MyActor), "MyActor")
  }
//  def makeActor() : ActorRef = system.actorOf(Props[MyActor], "MyActor")

  "My Actor" should {
    "throw if constructed with the wrong name" ignore new ActorSys {
      evaluating {
        val a = system.actorOf(Props[MyActor])
      } should produce [Exception]
    }
    "construct without exception" in new SubActorSys("Test1")  {
      val a = makeActor()
    }
    "respond with a Pong to a Ping" in new SubActorSys("Test2")  {
      val a = makeActor()
      a ! Ping
      expectMsg(Pong)

    }
  }

}
