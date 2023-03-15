import Dependencies._
import com.typesafe.sbt.packager.Keys.{ dockerBaseImage, packageName }

enablePlugins(JavaAppPackaging, AshScriptPlugin)

dockerBaseImage := "openjdk:11-jre-alpine"
Docker / packageName := "world"
dockerExposedPorts ++= Seq(9000, 9000)

libraryDependencies ++= Seq(
  Cats.core,
  Cats.effect,
  Http4s.server,
  Http4s.dsl,
  Http4s.circe,
  Circe.core,
  Circe.generic,
  Circe.parser,
  Specs2.core,
  Specs2.mockito,
  Skunk.core,
  Fs2.core,
  Fs2.io,
  Typesafe.config,
  Utils.ficus,
  Utils.logback
)
