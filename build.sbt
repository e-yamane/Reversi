import sbt._
import Keys._

name := "ReversiServer"
version := "0.0.1-SNAPSHOT"

scalaVersion := "2.13.8"

javacOptions ++= Seq("-encoding", "UTF-8")

libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "1.3.1",
  "net.arnx" % "jsonic" % "1.2.7",
  "org.apache.mina" % "mina-core" % "2.0.4",
  "org.slf4j" % "slf4j-api" % "1.6.1",
  "junit" % "junit" % "4.12" % Test
)

mainClass in (Compile, run) := Some("reversi.impl.SwingReporter")

// Eclipse plugin settings (requires sbt-eclipse plugin)
//EclipseKeys.projectFlavor := EclipseProjectFlavor.Java // Java project
//EclipseKeys.classpathContainers := Seq(
//  "org.springsource.ide.eclipse.gradle.classpathcontainer"
//)

// For application plugin, if using sbt-native-packager
enablePlugins(JavaAppPackaging)

fork in run := true

run / connectInput := true

