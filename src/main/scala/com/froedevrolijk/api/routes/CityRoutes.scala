package com.froedevrolijk.api.routes

import cats.FlatMap.ops._
import cats.effect.{ Async, ContextShift, Sync, Timer }
import com.froedevrolijk.api.db.datamodels.QueryCity
import com.froedevrolijk.api.exception.EmptyRequest
import com.froedevrolijk.api.service.CityService
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{ HttpRoutes, _ }

trait CityRoutes[F[_]] extends Http4sDsl[F] {

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
              requestCountryCode  <- req.as[QueryCity]
              filteredCountryCode <- filterEmptyRequestBody(requestCountryCode.countryCode)
              cities              <- queryCityService.findCitiesPerCountry(filteredCountryCode)
            } yield cities.asJson
            Ok(citiesOutput)

          case GET -> Root / "get-all" =>
            val result = for {
              cities <- queryCityService.selectAllCities
            } yield cities.asJson
            Ok(result)
        }

      //          case req @ PUT -> Root / "cities" =>
      //          val citiesOutput = for {
      //          name <- req.as[CityName]
      //
      //          }

      def filterEmptyRequestBody(s: String): F[String] =
        if (s == null || s.trim.isEmpty) F.raiseError[String](EmptyRequest("request body was empty"))
        else F.pure[String](s)
    }
}
