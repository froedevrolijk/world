package com.froedevrolijk.api.db.query

import cats.Applicative
import cats.effect.{ Async, Resource, Sync }
import com.froedevrolijk.api.db.datamodels.{ Country, QueryCountry }
import com.froedevrolijk.api.db.query.DBQueries._
import skunk.Session
import cats.FlatMap.ops._

trait CountryService[F[_]] {

  def findCountriesByName(args: QueryCountry): F[List[Country]]
}

object CountryService {

  def impl[F[_]: Sync](implicit S: Session[F]): CountryService[F] =
    new CountryService[F] {

      override def findCountriesByName(args: QueryCountry): F[List[Country]] = {
        val preparedQuery = S.prepare(countries)
        preparedQuery.use(_.stream(args.country, 32).compile.toList)
      }
    }
}
