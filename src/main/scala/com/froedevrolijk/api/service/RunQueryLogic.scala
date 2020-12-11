package com.froedevrolijk.api.service

import cats.FlatMap.ops._
import cats.effect.Sync
import com.froedevrolijk.api.exception.{
  CannotInsertException,
  CannotSelectException,
  CannotUpdateException,
  EmptyRowException
}
import skunk.{ Command, Query, Session }
import skunk.data.Completion._

object RunQueryLogic {

  def runQuery[F[_], A, B](pq: Query[A, B], inputType: A, chunkSize: Int = 32)(implicit
      S: Session[F],
      F: Sync[F]
  ): F[List[B]] =
    S.prepare(pq).use(_.stream(inputType, chunkSize).compile.toList)

  def runCommandDelete[F[_], A](ps: Command[A], inputType: A)(implicit
      S: Session[F],
      F: Sync[F]
  ): F[Unit] =
    S.prepare(ps)
      .use(_.execute(inputType).flatMap {
        case Delete(0) => F.raiseError(EmptyRowException("no such row in db"))
        case Delete(1) => F.pure(println("row deleted"))
        case c         => F.pure(println(c))
      })

  def runCommandUpdate[F[_], A](ps: Command[A], inputType: A)(implicit
      S: Session[F],
      F: Sync[F]
  ): F[Unit] =
    S.prepare(ps)
      .use(_.execute(inputType).flatMap {
        case Update(0) | Delete(0) => F.raiseError(CannotUpdateException("Not able to update"))
        case Update(1) | Delete(1) => F.pure(println("row updated"))
        case c                     => F.pure(println(c))
      })

  def runCommandInsert[F[_], A](ps: Command[A], inputType: A)(implicit
      S: Session[F],
      F: Sync[F]
  ): F[Unit] =
    S.prepare(ps)
      .use(_.execute(inputType).flatMap {
        case Insert(0) => F.raiseError(CannotInsertException("cannot insert into db"))
        case Insert(1) => F.pure(println("row inserted"))
        case c         => F.pure(println(c))
      })
}
