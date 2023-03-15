package com.froedevrolijk.api.session

import cats.effect.{ Resource, _ }
import com.typesafe.config.ConfigFactory
import natchez.Trace
import skunk.Session

trait RunSession[F[_]] {

  def session: Resource[F, Session[F]]

}

object RunSession {

  def impl[F[_]: ContextShift: ConcurrentEffect](implicit
      T: Trace[F]
  ): RunSession[F] =
    new RunSession[F] {

      override def session: Resource[F, Session[F]] = {

        val config = ConfigFactory.load()

        Session.single(
          host = config.getString("postgres.host"),
          user = config.getString("postgres.user"),
          database = config.getString("postgres.database"),
          password = Some(config.getString("postgres.password"))
        )
      }

    }

}
