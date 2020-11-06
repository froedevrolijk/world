package com.froedevrolijk.api.routes

import cats.FlatMap.ops._
import cats.effect.{ Async, ConcurrentEffect, ContextShift, Resource, Sync, Timer }
import com.froedevrolijk.api.db.command.CommandService
import com.froedevrolijk.api.db.datamodels.{ City, QueryCountry }
import com.froedevrolijk.api.session.RunSession
import io.circe.generic.auto._
import natchez.Trace.Implicits.noop
import org.http4s.circe.jsonOf
import org.http4s.dsl.Http4sDsl
import org.http4s.{ EntityDecoder, HttpRoutes }
import skunk.Session

trait CommandRoutes[F[_]] extends Http4sDsl[F] {

  def insertCitySingle: HttpRoutes[F]

  def insertCityMany: HttpRoutes[F]

}

object CommandRoutes {

  def impl[F[_]: Sync: Async: ContextShift: ConcurrentEffect: Timer](
//      commandService: CommandService[F]
  ): CommandRoutes[F] =
    new CommandRoutes[F] {

      implicit val cityDecoder: EntityDecoder[F, City]           = jsonOf[F, City]
      implicit val cityListDecoder: EntityDecoder[F, List[City]] = jsonOf[F, List[City]]

      def session: Resource[F, Session[F]] =
        for {
          s <- RunSession.impl[F].session
        } yield s

      override def insertCitySingle: HttpRoutes[F] =
        HttpRoutes.of[F] {
          case req @ POST -> Root / "add-city-single" =>
            val addOutput = for {
              city <- req.as[City]
              _    <- session.map(CommandService.impl[F](_)).use(s => s.insertSingle(city))
            } yield ()
            Ok(addOutput)
        }

      override def insertCityMany: HttpRoutes[F] =
        HttpRoutes.of[F] {
          case req @ POST -> Root / "add-city-many" =>
            val addOutput = for {
              cities <- req.as[List[City]]
              _      <- session.map(CommandService.impl[F](_)).use(s => s.insertMany(cities))
            } yield ()
            Ok(addOutput)
        }
    }
}
