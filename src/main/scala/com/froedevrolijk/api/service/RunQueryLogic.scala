package com.froedevrolijk.api.service

import cats.effect.{ Resource, Sync }
import com.froedevrolijk.api.db.datamodels.{ Country, QueryCountry }
import skunk.{ PreparedQuery, Query, Session }

object RunQueryLogic {

//  def runQuery2[F, A, B](q: Query[A, B])(implicit S: Session[F]): Resource[F, PreparedQuery[F, A, B]] =
//    S.prepare(q)

//  def runQuery[F[_], T, A, B](pq: Query[A, B], args: A)(implicit S: Session[F], F: Sync[F]): F[List[B]] =
//    S.prepare(pq).use(_.stream(args, 32).compile.toList)
//
//  def runQuery2[F[_]](pq: Query[String, Country], args: QueryCountry)(implicit
//      S: Session[F],
//      F: Sync[F]
//  ): F[List[Country]] =
//    S.prepare(pq).use(_.stream(args.country, 32).compile.toList)

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

}
