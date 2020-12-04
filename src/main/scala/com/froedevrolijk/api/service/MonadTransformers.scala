package com.froedevrolijk.api.service

import cats.data.EitherT
import com.froedevrolijk.api.exception.MyError

trait MonadTransformers[F[_]] {

  type ResultT[R[_], A] = EitherT[F, MyError, A]
  type DBResult[A]      = ResultT[F, A]

}
