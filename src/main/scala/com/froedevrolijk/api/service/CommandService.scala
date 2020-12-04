package com.froedevrolijk.api.service

import cats.effect.Sync
import cats.implicits._
import com.froedevrolijk.api.db.command.DBCommands._
import com.froedevrolijk.api.db.datamodels._
import com.froedevrolijk.api.db.queries.DBQueries.selectAllCitiesStmt
import com.froedevrolijk.api.utils.Log
import skunk.Session
//import cats.Functor.ops.toAllFunctorOps
import cats.syntax.applicativeError._

trait CommandService[F[_]] extends Log with MonadTransformers[F] {

  def insertSingleCity(city: City): F[Unit]
  def insertMultipleCities(cs: Cities): F[Unit]

//  def updateCityPopulation(args: UpdateCityPopulation): F[Unit]
//  def updateCountryPopulation(args: UpdateCountryPopulation): F[Unit]

  def deleteSingleCity(args: CityName): F[Unit]
  def deleteSingleCountry(args: QueryCountry): F[Unit]

}

object CommandService {

  def impl[F[_]: Sync](S: Session[F]): CommandService[F] =
    new CommandService[F] {

      override def insertSingleCity(city: City): F[Unit] =
        S.prepare(insertSingleCityStmt).use(_.execute(city)).void

      override def insertMultipleCities(cs: Cities): F[Unit] =
        S.prepare(insertMultipleCitiesStmt(cs.cities)).use(_.execute(cs.cities)).void

//      override def updateCityPopulation(args: UpdateCityPopulation): DBResult[City] = ???
//        for {
//          city        <- checkIfCityExists(args.city)
//          _           <- checkCanBeUpdated(city)
//          updatedCity <- updateCityOnDb(city)
//        } yield ()

//        S.prepare(updateCityPopulationStmt).use(_.execute(args).void)

//      override def updateCountryPopulation(args: UpdateCountryPopulation): F[Unit] =
//        S.prepare(updateCountryPopulationStmt).use(_.execute(args).void)

      override def deleteSingleCity(args: CityName): F[Unit] =
        S.prepare(deleteSingleCityStmt).use(_.execute(args.names).void)

      override def deleteSingleCountry(args: QueryCountry): F[Unit] =
        S.prepare(deleteSingleCountryStmt).use(_.execute(args.country).void)
    }

}
