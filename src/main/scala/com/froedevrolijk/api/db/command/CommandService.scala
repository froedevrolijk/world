package com.froedevrolijk.api.db.command

import cats.effect.Sync
import cats.implicits._
import com.froedevrolijk.api.db.command.DBCommands.{ all, insertMultiple, insertOne }
import com.froedevrolijk.api.db.datamodels.City
import skunk.Session

trait CommandService[F[_]] {

  def insertSingle(city: City): F[Unit]

  def insertMany(cs: List[City]): F[Unit]

  def selectAll: F[List[City]]
}

object CommandService {

  def impl[F[_]: Sync](S: Session[F]): CommandService[F] =
    new CommandService[F] {

      override def insertSingle(city: City): F[Unit] =
        S.prepare(insertOne).use(_.execute(city)).void

      override def insertMany(cs: List[City]): F[Unit] =
        S.prepare(insertMultiple(cs)).use(_.execute(cs)).void

      override def selectAll: F[List[City]] =
        S.execute(all)
    }

}
