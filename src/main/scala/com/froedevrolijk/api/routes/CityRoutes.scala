package com.froedevrolijk.api.routes

import cats.FlatMap.ops._
import cats.effect.{ Async, ContextShift, Timer }
import com.froedevrolijk.api.db.datamodels.QueryCity
import com.froedevrolijk.api.db.query.CityService
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{ HttpRoutes, _ }

trait CityRoutes[F[_]] extends Http4sDsl[F] {

  def getCities: HttpRoutes[F]

}

object CityRoutes {

  def impl[F[_]: Async: ContextShift: Timer](queryCityService: CityService[F]): CityRoutes[F] =
    new CityRoutes[F] {

      implicit val decoder: EntityDecoder[F, QueryCity] = jsonOf[F, QueryCity]

      override def getCities: HttpRoutes[F] =
        HttpRoutes.of[F] {
          case req @ POST -> Root / "cities" =>
            val citiesOutput = for {
              request <- req.as[QueryCity]
              cities  <- queryCityService.findCitiesPerCountry(request) //request.body.toString()
            } yield cities.asJson
            Ok(citiesOutput)
        }

    }
}
