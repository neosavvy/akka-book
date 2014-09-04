import sbt._
import sbt.Keys._

object AkkaBookBuild extends Build {

  lazy val akkaBook = Project(
    id = "akka-book",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "Akka Book",
      organization := "com.neosavvy",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.0",
      scalacOptions ++= Seq("-feature", "-deprecation"),
      resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
      libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.1.0",

        libraryDependencies += "org.scalatest" %% "scalatest" % "2.0.M6-SNAP9" % "test",
        libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.1.0",
        libraryDependencies += "com.typesafe.akka" %% "akka-actor"   % "2.1.0"
    )
  )
}
