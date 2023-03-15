package com.froedevrolijk.api.routes

import cats.effect.Sync
import com.froedevrolijk.api.exception.EmptyRequestException

trait InputValidation[F[_]] {

  def filterEmptyRequestBody(s: String)(implicit F: Sync[F]): F[String] =
    if (s == null || s.trim.isEmpty) F.raiseError[String](EmptyRequestException("request body was empty"))
    else F.pure[String](s)
}
