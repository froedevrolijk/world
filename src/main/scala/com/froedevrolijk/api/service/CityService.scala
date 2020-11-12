package com.froedevrolijk.api.service

import cats.effect.Sync
import com.froedevrolijk.api.db.datamodels.{ CityName, QueryCity }
import com.froedevrolijk.api.db.queries.DBQueries._
import skunk.Session

trait CityService[F[_]] {

  def findCitiesPerCountry(args: QueryCity): F[List[CityName]]
}

object CityService {

  def impl[F[_]: Sync](implicit S: Session[F]): CityService[F] =
    new CityService[F] {

      override def findCitiesPerCountry(args: QueryCity): F[List[CityName]] =
        S.prepare(cities).use(_.stream(args.city, 32).compile.toList)
    }
}
