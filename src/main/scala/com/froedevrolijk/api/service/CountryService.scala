package com.froedevrolijk.api.service

import cats.effect.Sync
import com.froedevrolijk.api.db.datamodels.{ Country, QueryCountry }
import com.froedevrolijk.api.db.queries.DBQueries._
import com.froedevrolijk.api.utils.Log
import skunk.Session
import cats.Functor.ops.toAllFunctorOps
import cats.syntax.applicativeError._

trait CountryService[F[_]] extends Log {

  def findCountriesByName(args: QueryCountry): F[List[Country]]
}

object CountryService {

  def impl[F[_]: Sync](implicit S: Session[F]): CountryService[F] =
    new CountryService[F] {

      override def findCountriesByName(args: QueryCountry): F[List[Country]] =
        S.prepare(countries)
          .use(_.stream(args.country, 32).compile.toList)
//          .map(_ => logger.info("found country"))
//          .handleError(e => logger.info(s"failed to get the city: ${e.getMessage}"))
    }
}
