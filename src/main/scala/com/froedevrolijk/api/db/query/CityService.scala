package com.froedevrolijk.api.db.query

import cats.Applicative
import cats.effect.Sync
import com.froedevrolijk.api.db.datamodels.{ CityName, QueryCity }
import com.froedevrolijk.api.db.query.DBQueries._
import skunk.Session

trait CityService[F[_]] {

  def findCitiesPerCountry(args: QueryCity): F[List[CityName]]
}

object CityService {

  def impl[F[_]: Sync](implicit S: Session[F]): CityService[F] =
    new CityService[F] {

      override def findCitiesPerCountry(args: QueryCity): F[List[CityName]] = {
        val preparedQuery = S.prepare(cities)
        preparedQuery.use(_.stream(args.city, 32).compile.toList)
      }
    }
}
