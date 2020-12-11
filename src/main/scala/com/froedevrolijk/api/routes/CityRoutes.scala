package com.froedevrolijk.api.routes

import cats.FlatMap.ops._
import cats.effect.{ Async, ContextShift, Sync, Timer }
import com.froedevrolijk.api.db.datamodels.QueryCity
import com.froedevrolijk.api.exception.EmptyRequestException
import com.froedevrolijk.api.service.CityService
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{ HttpRoutes, _ }

trait CityRoutes[F[_]] extends Http4sDsl[F] with InputValidation[F] {

  def cityQueries: HttpRoutes[F]
}

object CityRoutes {

  def impl[F[_]: Async: ContextShift: Timer](
      queryCityService: CityService[F]
  )(implicit F: Sync[F]): CityRoutes[F] =
    new CityRoutes[F] {

      implicit val decoder: EntityDecoder[F, QueryCity] = jsonOf[F, QueryCity]
//      implicit val decoder2: EntityDecoder[F, CityName] = jsonOf[F, CityName]

      override def cityQueries: HttpRoutes[F] =
        HttpRoutes.of[F] {
          case req @ POST -> Root / "cities" =>
            val citiesOutput = for {
              request         <- req.as[QueryCity]
              filteredRequest <- filterEmptyRequestBody(request.countryCode)
              cities          <- queryCityService.findCitiesPerCountry(filteredRequest)
            } yield cities.asJson
            Ok(citiesOutput)

          case GET -> Root / "get-all-cities" =>
            val result = for {
              cities <- queryCityService.getAllCities
            } yield cities.asJson
            Ok(result)
        }

      //          case req @ PUT -> Root / "cities" =>
      //          val citiesOutput = for {
      //          name <- req.as[CityName]
      //
      //          }

    }
}
