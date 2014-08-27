package com.neosavvy.avionics

import akka.actor.{Actor, ActorRef}

/**
 * Created by aparrish on 8/27/14.
 */
object EventSource {
  case class RegisterListener(listener : ActorRef)
  case class UnregisterListener(listener : ActorRef)
}

trait EventSource { this : Actor =>
  import EventSource._

  var listeners = Vector.empty[ActorRef]

  def sendEvent[T](event:T):Unit = listeners foreach {
    _ ! event
  }

  def eventSourceReceive : Receive = {
    case RegisterListener(listener) => listeners = listeners :+ listener
    case UnregisterListener(listener) => listeners = listeners filter { _ != listener }
  }

}
