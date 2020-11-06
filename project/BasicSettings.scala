import sbt.Keys._
import sbt._

object BasicSettings extends AutoPlugin {

  override lazy val trigger = allRequirements

  lazy val homeCredentials = Seq(
    Path.userHome / ".sbt" / "credentials"
  ) collect { case path if path.canRead => Credentials(path) }

  lazy val settings: Seq[Def.Setting[_]] = Seq(
    name := "world",
    version := "0.1",
    scalaVersion := "2.12.10",
    scalacOptions ++= Seq(
      "-deprecation",     // Emit warning and location for usages of deprecated APIs.
      "-feature",         // Emit warning and location for usages of features that should be imported explicitly.
      "-unchecked",       // Enable additional warnings where generated code depends on assumptions.
      "-Xfatal-warnings", // Fail the compilation if there are any warnings.
      "-encoding",
      "UTF-8",                         // Adding default encode type
      "-language:implicitConversions", // Otherwise the compiler will ask to import scala.language.implicitConversions
      "-language:higherKinds",         // Enable by default the high kinds for cats
      "-language:postfixOps",          // Enable by default post fix ops for cats
      "-Ypartial-unification"          // Add for http4s using Scala 2.12
    )
  )

  val itSettings = Defaults.itSettings ++ Seq(
    fork in IntegrationTest := true,
    parallelExecution in IntegrationTest := false,
    testForkedParallel in IntegrationTest := false,
    testOptions in IntegrationTest += Tests.Argument("sequential")
  )

  override def projectSettings: Seq[Setting[_]] =
    super.projectSettings ++ settings ++ itSettings

  override def projectConfigurations =
    super.projectConfigurations :+ IntegrationTest
}