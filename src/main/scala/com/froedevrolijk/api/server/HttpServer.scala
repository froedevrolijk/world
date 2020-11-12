package com.froedevrolijk.api.server

import cats.effect.{ ConcurrentEffect, ContextShift, Timer }
import com.froedevrolijk.api.config.{ AppConfig, ServerStoreConfig }
import com.froedevrolijk.api.db.query.Combinator
import net.ceedubs.ficus.Ficus.{ toFicusConfig, _ }
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.KleisliSyntax
import skunk.Session

import scala.concurrent.ExecutionContext

trait HttpServer[F[_]] extends KleisliSyntax with Http4sDsl[F] {
  def server(
      serverConfig: ServerStoreConfig,
      appConfig: AppConfig
  ): F[Unit]

}

object HttpServer {

  def impl[F[_]: ContextShift: ConcurrentEffect: Timer](implicit S: Session[F]): HttpServer[F] =
    new HttpServer[F] {

      override def server(
          serverStoreConfig: ServerStoreConfig,
          appConfig: AppConfig
      ): F[Unit] = {

        val routes = Combinator.impl[F].apiRoutesCombinator.orNotFound

        BlazeServerBuilder[F](ConcurrentEffect[F], Timer[F])
          .withHttpApp(routes)
          .bindHttp(appConfig.port, appConfig.address)
          .withExecutionContext(ExecutionContext.global)
      }.serve.compile.drain
    }

}
