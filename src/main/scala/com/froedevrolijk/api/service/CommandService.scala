package com.froedevrolijk.api.service

import java.util.UUID

import cats.Applicative
import cats.syntax.applicativeError._
import cats.effect.Sync
import com.froedevrolijk.api.db.datamodels._
import com.froedevrolijk.api.db.sqlstatements.Commands._
import com.froedevrolijk.api.utils.Log
import skunk.Session
import cats.Functor.ops.toAllFunctorOps

trait CommandService[F[_]] extends Log with RunSqlStatements[F] {

  def insertCity(city: City): F[Unit]
//  def insertMultipleCities(cs: Cities): F[Unit]

  def updateCity(args: UpdateCity): F[Unit]
  def updateCountry(args: UpdateCountry): F[Unit]

  def deleteCity(id: UUID): F[Unit]
  def deleteCountry(code: UUID): F[Unit]

}

object CommandService {

  def impl[F[_]: Sync](implicit S: Session[F], A: Applicative[F]): CommandService[F] =
    new CommandService[F] {

      override def insertCity(city: City): F[Unit] =
        runCommandInsert(insertCityStmt, city)
          .handleError(e => logger.error(s"an error occurred: ${e.getMessage}"))

      override def updateCity(args: UpdateCity): F[Unit] =
        runCommandUpdate(updateCityStmt, args)
          .handleError(e => logger.info(s"an error occurred ${e.getMessage}"))

      override def updateCountry(args: UpdateCountry): F[Unit] =
        runCommandUpdate(updateCountryStmt, args)

      override def deleteCity(id: UUID): F[Unit] =
        runCommandDelete(deleteCityStmt, id)

      override def deleteCountry(code: UUID): F[Unit] =
        runCommandDelete(deleteCountryStmt, code)
    }
}
