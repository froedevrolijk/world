package com.froedevrolijk.api.service

import cats.effect.Sync
import com.froedevrolijk.api.db.datamodels.{ CityName, QueryCity }
import com.froedevrolijk.api.db.queries.DBQueries._
import skunk.Session
import cats.FlatMap.ops._
import com.froedevrolijk.api.exception.CityNotFoundException
import cats.syntax.applicativeError._
import org.http4s.dsl.io.BadRequest

trait CityService[F[_]] {

  def findCitiesPerCountry(args: String): F[List[CityName]]

//  def foundOrNotFound()
}

object CityService {

  def impl[F[_]](implicit S: Session[F], F: Sync[F]): CityService[F] =
    new CityService[F] {

      override def findCitiesPerCountry(args: String): F[List[CityName]] =
        S.prepare(cities)
          .use(_.stream(args, 32).compile.toList)
          .flatMap(x => filterEmptyDatabaseResponse(x))

//      def filterEmptyDatabaseResponse(cities: List[CityName]): Either[Throwable, List[CityName]] =
//        cities match {
//          case Nil => Left(CityNotFoundException("city not found"))
//          case x   => Right(x)
//        }

      def filterEmptyDatabaseResponse[T](s: List[CityName]): F[T] =
        s match {
          case Nil => F.raiseError[T](CityNotFoundException("city was not found"))
          case x   => F.pure[T](x)
        }

    }

  // Use option for return from database?
  //

  // check if user sent a value. if yes, send this query to the service; if not, return a response saying that a value should be provided

  // check if there are cities for users' query. if yes, send back the list; if not, return a response saying there are no values for the users' query

}

//.handleErrorWith {
//          case CityNotFoundException("the city could not be found") => BadRequest(s"invalid argument")
//        }

//val output = for {
//              cities <- req.as[QueryCity]
//            } yield cities
//
//            output match {
//              case QueryCity(cityName) => Ok(cityName.asJson)
//              case _                   => Ok(CityNotFoundException("City could not be found"))
//            }
//        }
