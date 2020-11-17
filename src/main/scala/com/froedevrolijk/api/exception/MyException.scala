package com.froedevrolijk.api.exception

import scala.util.control.NoStackTrace

abstract class MyException(val message: String) extends RuntimeException(message)

case class CityNotFoundException(override val message: String) extends MyException(message) with NoStackTrace

case class CountryNotFoundException(override val message: String) extends MyException(message) with NoStackTrace

case class EmptyRequest(override val message: String) extends MyException(message) with NoStackTrace
