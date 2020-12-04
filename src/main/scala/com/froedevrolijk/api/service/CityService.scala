package com.froedevrolijk.api.service

import cats.effect.Sync
import com.froedevrolijk.api.db.datamodels.{ City, CityName }
import com.froedevrolijk.api.db.queries.DBQueries._
import com.froedevrolijk.api.utils.Log
import skunk.Session
import cats.Functor.ops.toAllFunctorOps
import cats.syntax.applicativeError._
import cats.FlatMap.ops._
import cats.data.EitherT
import com.froedevrolijk.api.exception.MyError

trait CityService[F[_]] extends Log with MonadTransformers[F] {

  def findCitiesPerCountry(args: String): F[List[CityName]]
  // F[Either[MyError, List[CityName]]]  // F[List[CityName]]
  //      F[Either[MyError, List[CityName]]] === EitherT[F, MyError, A]

  def selectAllCityNames(args: CityName): F[List[CityName]]

  def selectAllCities: F[List[City]]
}

object CityService {

  def impl[F[_]](implicit S: Session[F], F: Sync[F]): CityService[F] =
    new CityService[F] {

      override def findCitiesPerCountry(args: String): F[List[CityName]] = {
        val result = S.prepare(citiesStmt).use(_.stream(args, 32).compile.toList)
        result
        // (1) case list with values, (2) case empty list, (3) case an error occurred
        //        result.flatMap {
        //          case Nil => logger.info("There are no values for this request")
        //          case _   =>
        //        }
      }
      override def selectAllCityNames(args: CityName): F[List[CityName]] =
        S.prepare(selectAllCityNamesStmt).use(_.stream(args.names, 32).compile.toList)

      override def selectAllCities: F[List[City]] =
        S.execute(selectAllCitiesStmt)

    }
}
