package com.froedevrolijk.api.exception

import scala.util.control.NoStackTrace

abstract class MyException(val message: String) extends RuntimeException(message)

case class EmptyRequestException(override val message: String) extends MyException(message) with NoStackTrace

case class EmptyRowException(override val message: String) extends MyException(message) with NoStackTrace

case class CannotUpdateException(override val message: String) extends MyException(message) with NoStackTrace

case class CannotInsertException(override val message: String) extends MyException(message) with NoStackTrace
