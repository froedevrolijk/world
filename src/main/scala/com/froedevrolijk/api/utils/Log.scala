package com.froedevrolijk.api.utils

import org.slf4j.LoggerFactory

trait Log {
  val logger = LoggerFactory.getLogger(getClass)
}
