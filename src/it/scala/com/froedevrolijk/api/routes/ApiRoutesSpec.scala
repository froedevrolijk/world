//package com.froedevrolijk.api.routes
//
//import cats.effect.{ Bracket, ContextShift, IO, Resource, Timer }
//import com.froedevrolijk.api.session.RunSession
//import com.typesafe.config.ConfigFactory
//import natchez.Trace
//import natchez.Trace.Implicits.noop
//import org.http4s.HttpRoutes
//import org.specs2.mock.Mockito
//import org.specs2.mutable.Specification
//
//import scala.concurrent.ExecutionContext
//
//class ApiRoutesSpec extends Specification with Mockito {
//
////  def itSession[F[_]: ConcurrentEffect: ContextShift](s: Session[F])(implicit S: Sync[F], T: Trace[F]) = {
////    val acquire: Session[IO] = S.delay(RunSession.impl.session.use(s))
////    val release: Session[IO] => F[Unit] = session => S.delay(session.)
////  }
//
//  implicit val timer: Timer[IO]                = IO.timer(ExecutionContext.global)
//  implicit val contextShift: ContextShift[IO]  = IO.contextShift(ExecutionContext.global)
//  implicit val trace: Trace[IO]                = Trace[IO]
//  implicit val bracket: Bracket[IO, Throwable] = Bracket[IO, Throwable]
//
//  val config = ConfigFactory.load()
//  val resources = for {
//    session <- RunSession.impl.session
//  } yield session
//
////  val routes: Resource[IO, HttpRoutes[IO]] = resources.map { resources =>
////    implicit val session = resources
////  }
//
//  private val basePath     = "http://localhost:8086/"
//  private val endpointPath = "/keepalive"
//
//  "ApiRoutes" >> {
//    "healthRoute" should {
//      "return OK message for a healthy endpoint" in {}
//    }
//  }
//}
