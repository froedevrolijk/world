package com.froedevrolijk.api.routes

import cats.FlatMap.ops._
import cats.data.OptionT
import cats.effect.{ Async, ContextShift, Sync, Timer }
import com.froedevrolijk.api.db.datamodels.QueryCity
import com.froedevrolijk.api.exception.{ CityNotFoundException, EmptyRequest }
import com.froedevrolijk.api.service.CityService
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{ HttpRoutes, _ }
import cats.syntax.applicativeError._

trait CityRoutes[F[_]] extends Http4sDsl[F] {

  def cityQueries: HttpRoutes[F]

//  def checkIfEmpty(city: QueryCity): OptionT[F, T]
}

object CityRoutes {

  def impl[F[_]: Async: ContextShift: Timer](
      queryCityService: CityService[F]
  )(implicit F: Sync[F]): CityRoutes[F] =
    new CityRoutes[F] {

      implicit val decoder: EntityDecoder[F, QueryCity] = jsonOf[F, QueryCity]

      override def cityQueries: HttpRoutes[F] =
        HttpRoutes.of[F] {
          case req @ POST -> Root / "cities" =>
            val citiesOutput = for {
              request <- req.as[QueryCity]
              cities  <- queryCityService.findCitiesPerCountry(request.city)
            } yield cities.asJson
            Ok(citiesOutput)
        }

    }

}

// TODO
// - user input validation
// - check if data in database
// - log request

//          case req @ POST -> Root / "cities" =>
//            val citiesOutput = for {
//              request <- req.as[QueryCity]
//              cities  <- queryCityService.findCitiesPerCountry(request)
//            } yield cities.asJson
//            Ok(citiesOutput)
//        }
