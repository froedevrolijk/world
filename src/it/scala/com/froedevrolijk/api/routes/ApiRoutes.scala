//package com.froedevrolijk.api.routes
//
//import cats.effect.{ Async, ContextShift, IO, Resource, Timer }
//import com.froedevrolijk.api.service.ServiceCombinator
//import com.froedevrolijk.api.session.RunSession
//import natchez.Trace.Implicits.noop
//import org.http4s.implicits._
//import org.http4s._
//import org.specs2.mutable.Specification
//import skunk.Session
//
//import scala.concurrent.ExecutionContext
//
//trait ApiRoutes extends Specification {
//
//  implicit val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
//  implicit val timer: Timer[IO]               = IO.timer(ExecutionContext.global)
//
//  val resources: Resource[IO, Session[IO]] =
//    for {
//      s <- RunSession.impl[IO].session
//    } yield s
//
//  val routes: Resource[IO, HttpRoutes[IO]] = resources.map { resources =>
//    implicit val session: Session[IO] = resources
//
//    ServiceCombinator.impl[IO].apiRoutesCombinator
//  }
//
//  def createRequest(method: Method.SafeMethodWithBody, uri: String): Request[IO] =
//    Request[IO]()
//      .withUri(Uri.unsafeFromString(uri))
//      .withHeaders(Headers.of(Header.apply("content-type", "application/json")))
//      .withMethod(method)
//
//  implicit class RichRequest(request: Request[IO]) {
//    def compile(implicit routes: HttpRoutes[IO], A: Async[IO]): IO[Response[IO]] =
//      routes.orNotFound(request)
//  }
//}
