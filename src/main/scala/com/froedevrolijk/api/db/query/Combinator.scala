package com.froedevrolijk.api.db.query
import cats.MonoidK.ops.toAllMonoidKOps
import cats.effect.{ Async, _ }
import com.froedevrolijk.api.db.command.CommandService
import com.froedevrolijk.api.routes.{ CityRoutes, CommandRoutes, CountryRoutes, HealthRoute }
import net.ceedubs.ficus.Ficus.{ toFicusConfig, _ }
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import org.http4s.HttpRoutes
import skunk.Session

trait Combinator[F[_]] {

  def cityService: CityService[F]

  def countryService: CountryService[F]

  def healthRouteApi: HttpRoutes[F] //HealthRoute[F]

  def cityRoutes: HttpRoutes[F]

  def countryRoute: HttpRoutes[F] // CountryRoutes[F]

  def commandService: CommandService[F]

  def commandApi: HttpRoutes[F]

  def apiRoutesCombinator: HttpRoutes[F]

}

object Combinator {

  def impl[F[_]: Async: ContextShift: ConcurrentEffect: Timer](implicit
      S: Session[F],
      B: Bracket[F, Throwable]
  ): Combinator[F] =
    new Combinator[F] {

      override def cityService: CityService[F] =
        CityService.impl[F]

      override def countryService: CountryService[F] =
        CountryService.impl[F]

      override def healthRouteApi: HttpRoutes[F] =
        HealthRoute.impl[F].healthRoute

      override def cityRoutes: HttpRoutes[F] =
        CityRoutes.impl[F](cityService).cityQueries

      override def countryRoute: HttpRoutes[F] =
        CountryRoutes.impl[F](countryService).countryQueries

      override def commandService: CommandService[F] =
        CommandService.impl(S)

      override def commandApi: HttpRoutes[F] =
        CommandRoutes.impl[F].cityCommands

      override def apiRoutesCombinator: HttpRoutes[F] = {
        val routes = List(healthRouteApi, countryRoute, cityRoutes, commandApi)
        routes.foldLeft(HttpRoutes.empty[F])(_ <+> _)
      }
    }

}
