package com.froedevrolijk.api.routes

import cats.FlatMap.ops._
import cats.effect.{ Async, ConcurrentEffect, ContextShift, Resource, Sync, Timer }
import com.froedevrolijk.api.db.command.CommandService
import com.froedevrolijk.api.db.datamodels.{ Cities, City }
import com.froedevrolijk.api.session.RunSession
import io.circe._
import io.circe.generic.auto._
import io.circe.generic.semiauto.deriveDecoder
import io.circe.syntax.EncoderOps
import natchez.Trace.Implicits.noop
import org.http4s.circe.{ jsonEncoder, jsonOf, toMessageSynax }
import org.http4s.dsl.Http4sDsl
import org.http4s.{ EntityDecoder, HttpRoutes }
import skunk.Session

trait CommandRoutes[F[_]] extends Http4sDsl[F] {

  def cityCommands: HttpRoutes[F]
}

object CommandRoutes {

  def impl[F[_]: Sync: Async: ContextShift: ConcurrentEffect: Timer]
//      commandService: CommandService[F]
      : CommandRoutes[F] =
    new CommandRoutes[F] {

      implicit val cityDecoder: EntityDecoder[F, City] = jsonOf[F, City]
      implicit val decodeCityList: Decoder[Cities]     = deriveDecoder[Cities]

      def session: Resource[F, Session[F]] =
        for {
          s <- RunSession.impl[F].session
        } yield s

      override def cityCommands: HttpRoutes[F] =
        HttpRoutes.of[F] {
          case req @ POST -> Root / "add-city-single" =>
            val result = for {
              city <- req.as[City]
              _    <- session.map(CommandService.impl[F](_)).use(s => s.insertSingle(city))
            } yield ()
            Ok(result)

          case req @ POST -> Root / "add-city-many" =>
            val result = for {
              cities <- req.decodeJson
              _      <- session.map(CommandService.impl[F](_)).use(s => s.insertMany(cities))
            } yield ()
            Ok(result)

          case GET -> Root / "get-all" =>
            val result = for {
              cities <- session.map(CommandService.impl[F](_)).use(s => s.selectAll)
            } yield cities.asJson
            Ok(result)
        }
    }
}
