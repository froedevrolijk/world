package com.froedevrolijk.api.service

import cats.data.OptionT
import cats.effect.Sync
import com.froedevrolijk.api.db.datamodels.{ Country, SmallCountry }
import com.froedevrolijk.api.db.sqlstatements.Queries._
import skunk.Session
import cats.syntax.applicativeError._
import cats.FlatMap.ops._

trait CountryService[F[_]] extends RunSqlStatements[F] {

  def findCountriesByName(args: String): F[List[SmallCountry]]

  def getAllCountries: F[List[Country]]
}

object CountryService {

  def impl[F[_]: Sync](implicit S: Session[F]): CountryService[F] =
    new CountryService[F] {

      // as opt t
      override def findCountriesByName(args: String) = { // : OptionT[F, SmallCountry]
        val listRows = runQuery(countriesStmt, args)
        listRows
        //        val optiont = OptionT.liftF(listRows)
        //        optiont.value.flatMap {
        //          case (Some(List(e))) => Right(Option.apply(e))
      }

      //          .handleError(e => logger.info(s"an error occurred: ${e.getMessage}"))

      // as list
      override def getAllCountries: F[List[Country]] =
        S.execute(selectAllCountriesStmt)

    }
}
