package com.froedevrolijk.api.routes

import cats.FlatMap.ops._
import cats.effect.{ Async, ConcurrentEffect, ContextShift, Resource, Sync, Timer }
import com.froedevrolijk.api.db.datamodels.{ Cities, City, UpdateCity, UpdateCountry }
import com.froedevrolijk.api.service.CommandService
import com.froedevrolijk.api.session.RunSession
import io.circe._
import io.circe.generic.auto._
import io.circe.generic.semiauto.deriveDecoder
import natchez.Trace.Implicits.noop
import org.http4s.circe.jsonOf
import org.http4s.dsl.Http4sDsl
import org.http4s.{ EntityDecoder, HttpRoutes }
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
      implicit val updateCityDecoder: EntityDecoder[F, UpdateCity]       = jsonOf[F, UpdateCity]
      implicit val updateCountryDecoder: EntityDecoder[F, UpdateCountry] = jsonOf[F, UpdateCountry]
      implicit val decodeCityList: Decoder[Cities]                       = deriveDecoder[Cities]

      def session: Resource[F, Session[F]] =
        for {
          s <- RunSession.impl[F].session
        } yield s

      override def cityCommands: HttpRoutes[F] =
        HttpRoutes.of[F] {
          case req @ POST -> Root / "add-city" =>
            val result = for {
              city <- req.as[City]
              _    <- commandService.insertSingleCity(city)
              //session.map(CommandService.impl[F](_)).use(s => s.insertSingleCity(city))
            } yield ()
            Ok(result)

          case req @ PUT -> Root / "update-city" =>
            val result = for {
              updatedCity <- req.as[UpdateCity]
              _           <- commandService.updateCity(updatedCity)
            } yield ()
            Ok(result)

          case req @ PUT -> Root / "update-country" =>
            val result = for {
              updatedCountry <- req.as[UpdateCountry]
              _              <- commandService.updateCountry(updatedCountry)
            } yield ()
            Ok(result)

          case DELETE -> Root / "delete-city" / IntVar(cityId) =>
            commandService
              .deleteCity(cityId)
              .flatMap(_ => Ok())

          case DELETE -> Root / "delete-country" / countryCode =>
            commandService
              .deleteCountry(countryCode)
              .flatMap(_ => Ok()) // code, exception encoded
        }
    }
}
