package com.froedevrolijk.api.routes

import cats.effect.Async
import com.froedevrolijk.api.db.command.CommandService
import com.froedevrolijk.api.db.datamodels.{ City, QueryCommand, QueryCommandList }
import io.circe.generic.auto._
import org.http4s.{ EntityDecoder, HttpRoutes }
import org.http4s.circe.jsonOf
import org.http4s.dsl.Http4sDsl
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{ HttpRoutes, _ }
import cats.FlatMap.ops._
import cats.MonadError
import cats.implicits.catsKernelStdGroupForByte
import com.froedevrolijk.api.exception.NotFoundException
import io.circe.Json

trait CommandRoutes[F[_]] extends Http4sDsl[F] {

  def insertCity: HttpRoutes[F]

}

object CommandRoutes {

  def impl[F[_]: Async](commandService: CommandService[F]): CommandRoutes[F] =
    new CommandRoutes[F] {

      implicit val decoderList: EntityDecoder[F, City] = jsonOf[F, City]

      val testCity = City(1, "ac", "wef", "wr", 12)

      override def insertCity: HttpRoutes[F] =
        HttpRoutes.of[F] {
          case req @ POST -> Root / "add-city" =>
            for {
              city         <- req.as[City]
              insertedCity <- commandService.insertSingle(testCity)
            } yield insertedCity
            Ok("The value was inserted into the database")
          // TODO add row to database
          // TODO add success / fail handling

        }
    }
}

//            val a = req.as[QueryCommand].flatMap { v =>
//              v.req match {
//                case Some(value) => value.asJson.noSpaces.to)
//                case None        =>
//              }
//            }

//              request <- req.as[QueryCommand] // TODO handle Option
//              request2 <- request.req match {
//                case Some(value) => M.pure(commandService.insertSingle(testCity))
//                case _           => M.raiseError(NotFoundException("No value was provided"))
//              }
//            } yield request2
//            Ok("The value was inserted into the database")
