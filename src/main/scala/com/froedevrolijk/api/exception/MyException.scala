package com.froedevrolijk.api.exception

import scala.util.control.NoStackTrace

abstract class MyException(val message: String) extends RuntimeException(message)

case class NotFoundException(override val message: String) extends MyException(message) with NoStackTrace
