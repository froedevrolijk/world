package com.froedevrolijk.api.service

import cats.effect.Sync
import com.froedevrolijk.api.db.datamodels.CityName
import com.froedevrolijk.api.db.queries.DBQueries._
import com.froedevrolijk.api.utils.Log
import skunk.Session
import cats.Functor.ops.toAllFunctorOps
import cats.syntax.applicativeError._

trait CityService[F[_]] extends Log {

  def findCitiesPerCountry(args: String): F[List[CityName]]

}

object CityService {

  def impl[F[_]](implicit S: Session[F], F: Sync[F]): CityService[F] =
    new CityService[F] {

      override def findCitiesPerCountry(args: String): F[List[CityName]] =
        S.prepare(cities)
          .use(_.stream(args, 32).compile.toList)
//          .map(_ => logger.info("found country"))
//          .handleError(e => logger.info(s"failed to get the city: ${e.getMessage}"))
    }
}
