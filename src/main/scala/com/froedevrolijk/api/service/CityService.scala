package com.froedevrolijk.api.service

import cats.effect.Sync
import com.froedevrolijk.api.db.datamodels.CityName
import com.froedevrolijk.api.db.queries.DBQueries._
import skunk.Session

trait CityService[F[_]] {

  def findCitiesPerCountry(args: String): F[List[CityName]]
}

object CityService {

  def impl[F[_]](implicit S: Session[F], F: Sync[F]): CityService[F] =
    new CityService[F] {

      override def findCitiesPerCountry(args: String): F[List[CityName]] =
        S.prepare(cities).use(_.stream(args, 32).compile.toList)
    }
}
