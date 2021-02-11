package com.froedevrolijk.api.routes

import org.http4s.{ Method, Status }
import org.specs2.mutable.Specification

class CityRoutesSpec extends Specification with ApiRoutes {

  private val path = "/get-all-cities"

  "CityRoutes" should {
    "return a list of cities" in {
      routes.use { implicit r =>
        val testResult = for {
          response <- createRequest(Method.GET, path).compile
        } yield response
        testResult must beEqualTo()
      }

//       TODO create json output that is expected

    }
  }
}
