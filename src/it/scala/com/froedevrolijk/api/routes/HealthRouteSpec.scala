//package com.froedevrolijk.api.routes
//
//import org.http4s.{ Method, Status }
//import org.specs2.mutable.Specification
//
//class HealthRouteSpec extends Specification with ApiRoutes {
//
//  private val path = "/keepalive"
//
//  "HealthRoute" should {
//    "return OK for a healthy endpoint" >>
//    routes
//      .use { implicit r =>
//        for {
//          response <- createRequest(Method.GET, path).compile
//        } yield response.status must beEqualTo(Status.Ok)
//      }
//      .unsafeRunSync()
//  }
//}
