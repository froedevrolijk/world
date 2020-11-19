package com.froedevrolijk.api.server

import cats.effect.{ ConcurrentEffect, ContextShift, Sync, Timer }
import com.froedevrolijk.api.config.{ AppConfig, ServerDebugConfig }
import com.froedevrolijk.api.service.ServiceCombinator
import net.ceedubs.ficus.Ficus.{ toFicusConfig, _ }
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware._
import org.http4s.syntax.KleisliSyntax
import skunk.Session

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

trait HttpServer[F[_]] extends KleisliSyntax with Http4sDsl[F] {
  def server(
      appConfig: AppConfig,
      serverDebugConfig: ServerDebugConfig
  ): F[Unit]

}

object HttpServer {

  def impl[F[_]: ContextShift: ConcurrentEffect: Timer](implicit F: Sync[F], S: Session[F]): HttpServer[F] =
    new HttpServer[F] {

      override def server(
          appConfig: AppConfig,
          serverDebugConfig: ServerDebugConfig
      ): F[Unit] = {

//        val logAction: Option[String => F[Unit]] =
//          if (serverDebugConfig.logAction) None
//          else Some(_ => F.unit)
//
//        val corsRoutes = Logger.httpApp(
//          logHeaders = serverDebugConfig.header,
//          logBody = serverDebugConfig.body,
//          logAction = logAction
//        )

        val methodConfig = CORSConfig(
          anyOrigin = true,
          anyMethod = false,
          allowedMethods = Some(Set("GET", "POST")),
          allowCredentials = true,
          maxAge = 1.day.toSeconds
        )

        val corsRoutes = CORS(ServiceCombinator.impl[F].apiRoutesCombinator.orNotFound, config = methodConfig)

        BlazeServerBuilder[F](ConcurrentEffect[F], Timer[F])
          .withHttpApp(corsRoutes)
          .bindHttp(appConfig.port, appConfig.address)
          .withExecutionContext(ExecutionContext.global)
      }.serve.compile.drain
    }

}
