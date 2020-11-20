package com.froedevrolijk.api

import cats.effect.{ ExitCode, IO, IOApp, Resource }
import natchez.Trace.Implicits.noop
import skunk.codec.all.{ bpchar, varchar, _ }
import skunk.implicits._
import skunk.{ ~, Decoder, Query, Session }

object Demo extends IOApp {

  val session: Resource[IO, Session[IO]] =
    Session.single(
      host = "localhost",
      port = 5432,
      user = "user",
      database = "world",
      password = Some("password"),
      debug = true
    )

  case class DemoCountry(code: String, name: String, population: Int)

  val country: Decoder[DemoCountry] =
    (bpchar(3) ~ varchar ~ int4).map { case c ~ n ~ p => DemoCountry(c, n, p) } // define a Decoder based on the bpchar, varchar and int4 decoder

  val select: Query[String, DemoCountry] =
    sql"""
       select code, name, population
       from country
       WHERE name like $varchar
       """.query(country)

  def run(args: List[String]): IO[ExitCode] =
    session.use { s =>
      s.prepare(select).use { pq =>
        pq.stream("A%", 8)                             // outputs a Stream of DemoCountry (Stream[IO, DemoCountry]
          .evalMap(c => IO(println(s"country -> $c"))) // print to the console Stream[IO, Unit]
          .compile                                     // Stram.CompileOps[IO, IO, Unit]
          .drain                                       // IO[Unit]
      }
    } as ExitCode.Success
}
