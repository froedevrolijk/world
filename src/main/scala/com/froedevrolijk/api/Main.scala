package com.froedevrolijk.api

import cats.effect.{ ConcurrentEffect, ContextShift, ExitCode, IO, IOApp, Sync, Timer }
import com.froedevrolijk.api.config.{ AppConfig, ServerStoreConfig }
import com.froedevrolijk.api.server.HttpServer
import com.froedevrolijk.api.session.RunSession
import com.froedevrolijk.api.utils.Log
import com.typesafe.config.ConfigFactory
import natchez.Trace
import net.ceedubs.ficus.Ficus.{ toFicusConfig, _ }
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import skunk.Session
import natchez.Trace.Implicits.noop
import cats.FlatMap.ops._

object Main extends IOApp with Log {

  override def run(args: List[String]): IO[ExitCode] =
    app[IO].as(ExitCode.Success)

  private def app[F[_]: ContextShift: ConcurrentEffect: Timer](implicit S: Sync[F]): F[Unit] = {

    val resources = for {
      session <- RunSession.impl[F].session
    } yield session

    resources.use { r =>
      implicit val session: Session[F] = r

      val serverConfigStore = ConfigFactory.load().getConfig("store").as[ServerStoreConfig]
      val appConfig         = ConfigFactory.load().getConfig("api").as[AppConfig]
      val api = HttpServer
        .impl[F]
        .server(serverConfig = serverConfigStore, appConfig = appConfig)
      api

    // ADD COMBINATOR

    }

  }

}

/*
 * - session.use...
 * - certificates / sslcontext
 * - run server
 * - github
 * */

// - Add types for query