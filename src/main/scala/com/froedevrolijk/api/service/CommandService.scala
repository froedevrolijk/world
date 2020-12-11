package com.froedevrolijk.api.service

import cats.Applicative
import cats.effect.Sync
import com.froedevrolijk.api.db.command.DBCommands._
import com.froedevrolijk.api.db.datamodels._
import com.froedevrolijk.api.service.RunQueryLogic.{ runCommandDelete, runCommandInsert, runCommandUpdate }
import com.froedevrolijk.api.utils.Log
import skunk.Session
//import cats.Functor.ops.toAllFunctorOps

trait CommandService[F[_]] extends Log {

  def insertSingleCity(city: City): F[Unit]
//  def insertMultipleCities(cs: Cities): F[Unit]

  def updateCity(args: UpdateCity): F[Unit]
  def updateCountry(args: UpdateCountry): F[Unit]

  def deleteCity(id: Int): F[Unit]
  def deleteCountry(code: String): F[Unit]

}

object CommandService {

  def impl[F[_]: Sync](implicit S: Session[F], A: Applicative[F]): CommandService[F] =
    new CommandService[F] {

      override def insertSingleCity(city: City): F[Unit] =
        runCommandInsert(insertSingleCityStmt, city)

      override def updateCity(args: UpdateCity): F[Unit] =
        runCommandUpdate(updateCityStmt, args)

      // TODO
      //  DELETE
      //  - check if city/country row exists based on id/code
      //      - return message to client that says the row does not exist
      //  - delete the value
      //      - log unsuccessful deletion
      //      - return message to client saying the row has been deleted
      //  UPDATE
      //  - check if city/country row exists based on id/code
      //      - return message to client that says the row does not exist
      //  - update the value
      //      - log unsuccessful deletion
      //      - return message to client saying the row has been updated

      // TODO deletion only visible after restart server

      //        for {
      //          city        <- checkIfCityExists(args.city)
      //          _           <- checkCanBeUpdated(city)
      //          updatedCity <- updateCityOnDb(city)
      //        } yield ()

      //        S.prepare(updateCityPopulationStmt).use(_.execute(args).void)

      override def updateCountry(args: UpdateCountry): F[Unit] =
        runCommandUpdate(updateCountryStmt, args)

      override def deleteCity(id: Int): F[Unit] =
        runCommandDelete(deleteCityStmt, id)

      override def deleteCountry(code: String): F[Unit] =
        runCommandDelete(deleteCountryStmt, code)
    }
}
