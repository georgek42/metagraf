name := """metagraf"""
organization := "io.metagraf"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.2"

libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.0" % Test

libraryDependencies += "de.sciss" % "sphinx4-core" % "1.0.0"
libraryDependencies += "de.sciss" % "sphinx4-data" % "1.0.0"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "io.metagraf.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "io.metagraf.binders._"
