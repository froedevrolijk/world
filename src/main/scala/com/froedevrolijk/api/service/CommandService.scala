package com.froedevrolijk.api.service

import cats.effect.Sync
import com.froedevrolijk.api.db.command.DBCommands._
import com.froedevrolijk.api.db.datamodels._
import com.froedevrolijk.api.service.RunQueryLogic.runCommand
import com.froedevrolijk.api.utils.Log
import skunk.Session
//import cats.Functor.ops.toAllFunctorOps

trait CommandService[F[_]] extends Log with MonadTransformers[F] {

  def insertSingleCity(city: City): F[Unit]
//  def insertMultipleCities(cs: Cities): F[Unit]

  def updateCityPopulation(args: UpdateCity): F[Unit]
  def updateCountryPopulation(args: UpdateCountry): F[Unit]

  def deleteSingleCity(id: Int): F[Unit]
  def deleteSingleCountry(code: String): F[Unit]

}

object CommandService {

  def impl[F[_]: Sync](implicit S: Session[F]): CommandService[F] =
    new CommandService[F] {

      override def insertSingleCity(city: City): F[Unit] =
        runCommand(insertSingleCityStmt, city)

      override def updateCityPopulation(args: UpdateCity): F[Unit] =
        runCommand(updateCityStmt, args)
//        for {
//          city        <- checkIfCityExists(args.city)
//          _           <- checkCanBeUpdated(city)
//          updatedCity <- updateCityOnDb(city)
//        } yield ()

//        S.prepare(updateCityPopulationStmt).use(_.execute(args).void)

      override def updateCountryPopulation(args: UpdateCountry): F[Unit] =
        runCommand(updateCountryStmt, args)

      override def deleteSingleCity(id: Int): F[Unit] =
        runCommand(deleteSingleCityStmt, id)

      override def deleteSingleCountry(code: String): F[Unit] =
        runCommand(deleteSingleCountryStmt, code)
    }

}
