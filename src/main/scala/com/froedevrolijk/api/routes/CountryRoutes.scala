package com.froedevrolijk.api.routes

import cats.FlatMap.ops._
import cats.effect.Async
import com.froedevrolijk.api.db.datamodels.QueryCountry
import com.froedevrolijk.api.db.query.CountryService
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.{ HttpRoutes, _ }
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

trait CountryRoutes[F[_]] extends Http4sDsl[F] {

  def getCountries: HttpRoutes[F]

  //  def postCountries: HttpRoutes[F]
}

object CountryRoutes {

  def impl[F[_]: Async](queryCountryService: CountryService[F]): CountryRoutes[F] =
    new CountryRoutes[F] {

      implicit val decoder: EntityDecoder[F, QueryCountry] = jsonOf[F, QueryCountry]

      override def getCountries: HttpRoutes[F] =
        HttpRoutes.of[F] {
          case req @ POST -> Root / "countries" =>
            val countriesOutput = for {
              request   <- req.as[QueryCountry]
              countries <- queryCountryService.findCountriesByName(request)
            } yield countries.asJson
            Ok(countriesOutput)
        }

      //    override def postCountries: HttpRoutes[F] = ???
    }
}
