package com.froedevrolijk.api.routes

import cats.FlatMap.ops._
import cats.effect.Async
import com.froedevrolijk.api.db.datamodels.QueryCountry
import com.froedevrolijk.api.service.CountryService
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{ HttpRoutes, _ }

trait CountryRoutes[F[_]] extends Http4sDsl[F] with InputValidation[F] {

  def countryQueries: HttpRoutes[F]
}

object CountryRoutes {

  def impl[F[_]: Async](queryCountryService: CountryService[F]): CountryRoutes[F] =
    new CountryRoutes[F] {

      implicit val decoder: EntityDecoder[F, QueryCountry] = jsonOf[F, QueryCountry]

      override def countryQueries: HttpRoutes[F] =
        HttpRoutes.of[F] {
          case req @ POST -> Root / "countries" =>
            val countriesOutput = for {
              request         <- req.as[QueryCountry]
              filteredRequest <- filterEmptyRequestBody(request.country)
              countries       <- queryCountryService.findCountriesByName(filteredRequest)
            } yield countries.asJson
            Ok(countriesOutput)

          case GET -> Root / "get-all-countries" =>
            val result = for {
              cities <- queryCountryService.getAllCountries
            } yield cities.asJson
            Ok(result)
        }
    }
}
