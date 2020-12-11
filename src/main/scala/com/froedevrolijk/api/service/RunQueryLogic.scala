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

  /**
   * Parameterized function to run queries from the database.
   * @param pq PreparedQuery in Skunk
   * @param inputType The input type used in the Skunk SQL Query, i.e. this is a interpolated value called varchar, which has type Encoder[String]
   * @param chunkSize Size of each FS2 Stream chunk
   * @tparam A The value that will be encoded to be used in the Query
   * @tparam B The values that will be decoded to be converted into a Scala case class
   * @return
   */
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
