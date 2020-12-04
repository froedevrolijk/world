package com.froedevrolijk.api.service

import cats.effect.Sync
import com.froedevrolijk.api.db.datamodels.{ Country, QueryCountry }
import com.froedevrolijk.api.db.queries.DBQueries._
import com.froedevrolijk.api.service.RunQueryLogic.runQuery
import skunk.Session

trait CountryService[F[_]] {

//  def findCountriesByName[T](args: QueryCountry): F[List[T]]

//  def findCountriesByName2[T](args: QueryCountry): F[List[Country]]

  def findCountriesByName(args: String): F[List[Country]]
}

object CountryService {

  def impl[F[_]: Sync](implicit S: Session[F]): CountryService[F] =
    new CountryService[F] {

//      override def findCountriesByName[T](args: QueryCountry): F[List[T]] =
//        runQuery2(countriesStmt).use(_.stream(args.country, 32).compile.toList)

      override def findCountriesByName(args: String): F[List[Country]] =
        runQuery(countriesStmt, args)

    }
}
