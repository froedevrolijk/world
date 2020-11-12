package com.froedevrolijk.api.service

import cats.effect.Sync
import com.froedevrolijk.api.db.datamodels.{ Country, QueryCountry }
import com.froedevrolijk.api.db.queries.DBQueries._
import skunk.Session

trait CountryService[F[_]] {

  def findCountriesByName(args: QueryCountry): F[List[Country]]
}

object CountryService {

  def impl[F[_]: Sync](implicit S: Session[F]): CountryService[F] =
    new CountryService[F] {

      override def findCountriesByName(args: QueryCountry): F[List[Country]] =
        S.prepare(countries).use(_.stream(args.country, 32).compile.toList)
    }
}
