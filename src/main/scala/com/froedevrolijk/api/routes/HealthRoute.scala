package com.froedevrolijk.api.routes

import cats.effect.Async
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

trait HealthRoute[F[_]] extends Http4sDsl[F] {
  def healthRoute: HttpRoutes[F]

}

object HealthRoute {

  def impl[F[_]: Async]: HealthRoute[F] =
    new HealthRoute[F] {

      override def healthRoute: HttpRoutes[F] =
        HttpRoutes.of {
          case GET -> Root / "keepalive" => Ok("Keepalive OK")
        }

    }

}
