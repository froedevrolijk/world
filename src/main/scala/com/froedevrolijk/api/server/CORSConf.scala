package com.froedevrolijk.api.server

import org.http4s.server.middleware.CORSConfig
import scala.concurrent.duration.DurationInt

object CORSConf {

  val methodConfig: CORSConfig = CORSConfig(
    anyOrigin = true,
    anyMethod = false,
    allowedMethods = Some(Set("GET", "POST")),
    allowCredentials = true,
    maxAge = 1.day.toSeconds
  )
}
