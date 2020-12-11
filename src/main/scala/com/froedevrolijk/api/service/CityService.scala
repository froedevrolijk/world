package com.froedevrolijk.api.service

import cats.effect.Sync
import com.froedevrolijk.api.db.datamodels.{ City, CityName }
import com.froedevrolijk.api.db.queries.DBQueries._
import com.froedevrolijk.api.service.RunQueryLogic.runQuery
import com.froedevrolijk.api.utils.Log
import skunk.Session

import cats.effect._
import skunk._
import skunk.implicits._
import skunk.codec.all._
import java.time.OffsetDateTime
import natchez.Trace.Implicits.noop

trait CityService[F[_]] extends Log with MonadTransformers[F] {

  def findCitiesPerCountry(args: String): F[List[CityName]]
  // F[Either[MyError, List[CityName]]]  // F[List[CityName]]
  //      F[Either[MyError, List[CityName]]] === EitherT[F, MyError, A]

  def getAllCityNames(args: String): F[List[CityName]]

  def getAllCities: F[List[City]]
}

object CityService {

  def impl[F[_]](implicit S: Session[F], F: Sync[F]): CityService[F] =
    new CityService[F] {

      override def findCitiesPerCountry(args: String): F[List[CityName]] =
        runQuery(citiesStmt, args)
      // (1) case list with values, (2) case empty list, (3) case an error occurred
      //        result.flatMap {
      //          case Nil => logger.info("There are no values for this request")
      //          case _   =>
      //        }
      override def getAllCityNames(args: String): F[List[CityName]] =
        runQuery(selectAllCityNamesStmt, args)

      override def getAllCities: F[List[City]] =
        S.execute(selectAllCitiesStmt)

    }
}
