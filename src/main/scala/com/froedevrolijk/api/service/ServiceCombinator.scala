package com.froedevrolijk.api.service

import cats.MonoidK.ops.toAllMonoidKOps
import cats.effect.{Async, _}
import com.froedevrolijk.api.routes.{CityRoutes, CommandRoutes, CountryRoutes, HealthRoute}
import net.ceedubs.ficus.Ficus.{toFicusConfig, _}
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import org.http4s.HttpRoutes
import skunk.Session

trait ServiceCombinator[F[_]] {

  def healthRouteApi: HttpRoutes[F]
  def cityService: CityService[F]
  def cityRoutes: HttpRoutes[F]
  def countryService: CountryService[F]
  def countryRoutes: HttpRoutes[F]
  def commandService: CommandService[F]
  def commandRoutes: HttpRoutes[F]
  def apiRoutesCombinator: HttpRoutes[F]
}

object ServiceCombinator {

  def impl[F[_]: Async: ContextShift: ConcurrentEffect: Timer](implicit
      S: Session[F],
  ): ServiceCombinator[F] =
    new ServiceCombinator[F] {

      override def healthRouteApi: HttpRoutes[F] =
        HealthRoute.impl[F].healthRoute

      override def cityService: CityService[F] =
        CityService.impl[F]

      override def cityRoutes: HttpRoutes[F] =
        CityRoutes.impl[F](cityService).cityQueries

      override def countryService: CountryService[F] =
        CountryService.impl[F]

      override def countryRoutes: HttpRoutes[F] =
        CountryRoutes.impl[F](countryService).countryQueries

      override def commandService: CommandService[F] =
        CommandService.impl[F]

      override def commandRoutes: HttpRoutes[F] =
        CommandRoutes.impl[F](commandService).cityCommands

      override def apiRoutesCombinator: HttpRoutes[F] = {
        val routes = List(healthRouteApi, countryRoutes, cityRoutes, commandRoutes)
        routes.foldLeft(HttpRoutes.empty[F])(_ <+> _)
      }
    }

}
