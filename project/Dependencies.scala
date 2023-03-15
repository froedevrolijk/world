import sbt._

object Dependencies {

  val autoImport = this

  object Cats {
    val version = "2.1.1"
    val core    = "org.typelevel" %% "cats-core"   % version
    val effect  = "org.typelevel" %% "cats-effect" % version
  }

  object Http4s {
    val version = "0.21.1"
    val server  = "org.http4s" %% "http4s-blaze-server" % version
    val dsl     = "org.http4s" %% "http4s-dsl"          % version
    val circe   = "org.http4s" %% "http4s-circe"        % version
  }

  object Circe {
    val version = "0.12.3"
    val core    = "io.circe" %% "circe-core"    % version
    val generic = "io.circe" %% "circe-generic" % version
    val parser  = "io.circe" %% "circe-parser"  % version
  }

  object Specs2 {
    val core    = "org.specs2" %% "specs2-core" % "4.10.3"
    val mockito = "org.specs2" %% "specs2-mock" % "4.5.1" % "test"
  }

  object Skunk {
    val core = "org.tpolecat" %% "skunk-core" % "0.0.11"
  }

  object Fs2 {
    val version = "2.4.2"
    val core    = "co.fs2" %% "fs2-core" % version
    val io      = "co.fs2" %% "fs2-io"   % version
  }

  object Typesafe {
    val config = "com.typesafe" % "config" % "1.4.0"
  }

  object Utils {
    val ficus   = "com.iheart"    %% "ficus"           % "1.5.0"
    val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
  }

}
