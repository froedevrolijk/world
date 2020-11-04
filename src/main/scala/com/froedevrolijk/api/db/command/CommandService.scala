package com.froedevrolijk.api.db.command

import cats.effect.{ Bracket, Sync }
import com.froedevrolijk.api.db.command.DBCommands.{ all, insertMultiple, insertOne }
import com.froedevrolijk.api.db.datamodels.City
import skunk.Session
import skunk.data.Completion
import com.froedevrolijk.api.db.command._

trait CommandService[F[_]] {

  def insertSingle(city: City): F[Completion]

  def insertMany(cs: List[City]): F[Completion]

  def selectAll: F[List[City]]
}

object CommandService {

  def impl[F[_]: Sync](implicit S: Session[F]): CommandService[F] =
    new CommandService[F] {

      override def insertSingle(city: City): F[Completion] =
        S.prepare(insertOne).use(_.execute(city))

      override def insertMany(cs: List[City]): F[Completion] =
        S.prepare(insertMultiple(cs)).use(_.execute(cs))

      override def selectAll: F[List[City]] =
        S.execute(all)
    }

}
