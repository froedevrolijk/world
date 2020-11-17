package com.froedevrolijk.api.utils

import cats.data.OptionT
import cats.effect.Sync
import com.froedevrolijk.api.exception.EmptyRequest
import cats.FlatMap.ops._

trait Implicits {

  implicit class RichOptT[F[_], T](optionT: OptionT[F, T]) {

    def orNotFound(implicit F: Sync[F]): F[T] =
      optionT.value.flatMap {
        case Some(v) => F.pure[T](v)
        case None    => F.raiseError[T](EmptyRequest("empty request"))
      }
  }

}
