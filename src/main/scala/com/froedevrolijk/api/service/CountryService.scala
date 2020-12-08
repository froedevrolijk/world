package com.froedevrolijk.api.service

import cats.effect.Sync
import com.froedevrolijk.api.db.datamodels.{ Country, Country2, CountryFull }
import com.froedevrolijk.api.db.queries.DBQueries._
import com.froedevrolijk.api.service.RunQueryLogic.runQuery
import skunk.Session

trait CountryService[F[_]] {

  def findCountriesByName(args: String): F[List[Country]]

  def getAllCountries: F[List[Country2]]
}

object CountryService {

  def impl[F[_]: Sync](implicit S: Session[F]): CountryService[F] =
    new CountryService[F] {

      override def findCountriesByName(args: String): F[List[Country]] =
        runQuery(countriesStmt, args)

      override def getAllCountries: F[List[Country2]] =
        S.execute(selectAllCountriesStmt)
    }
}
