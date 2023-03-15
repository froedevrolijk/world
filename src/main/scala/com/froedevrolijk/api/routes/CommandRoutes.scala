package com.froedevrolijk.api.routes

import cats.FlatMap.ops._
import cats.effect.{Async, ConcurrentEffect, ContextShift, Resource, Sync, Timer}
import com.froedevrolijk.api.db.datamodels._
import com.froedevrolijk.api.service.CommandService
import com.froedevrolijk.api.session.RunSession
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import natchez.Trace.Implicits.noop
import org.http4s.circe.{jsonOf, _}
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes}
import skunk.Session

trait CommandRoutes[F[_]] extends Http4sDsl[F] {

  def cityCommands: HttpRoutes[F]
}

object CommandRoutes {

  def impl[F[_]: Sync: Async: ContextShift: ConcurrentEffect: Timer](
      commandService: CommandService[F]
  ): CommandRoutes[F] =
    new CommandRoutes[F] {

      implicit val cityDecoder: EntityDecoder[F, City]                   = jsonOf[F, City]
      implicit val updateCountryDecoder: EntityDecoder[F, UpdateCountryMinor]  = jsonOf[F, UpdateCountryMinor]

      def session: Resource[F, Session[F]] =
        for {
          s <- RunSession.impl[F].session
        } yield s

      override def cityCommands: HttpRoutes[F] =
        HttpRoutes.of[F] {
          case req @ POST -> Root / "add-city" =>
            val result = for {
              city <- req.as[City]
              _    <- commandService.insertCity(city)
            } yield ().asJson
            Ok(result)

          case req @ PUT -> Root / "update-country" / UUIDVar(countryCode) =>
            val result = for {
              country <- req.as[UpdateCountryMinor]
              _       <- commandService.updateCountry(country, countryCode)
            } yield ().asJson
            Ok(result)

          case DELETE -> Root / "delete-country" / UUIDVar(countryCode) =>
            commandService
              .deleteCountry(countryCode)
              .flatMap(_ => Ok())
        }
    }
}
