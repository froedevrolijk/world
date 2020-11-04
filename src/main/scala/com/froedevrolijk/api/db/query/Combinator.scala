package com.froedevrolijk.api.db.query
import cats.effect.{ Async, Resource, _ }
import com.froedevrolijk.api.db.datamodels.Country
import com.froedevrolijk.api.routes.{ CityRoutes, CommandRoutes, CountryRoutes, HealthRoute }
import net.ceedubs.ficus.Ficus.{ toFicusConfig, _ }
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import skunk.Session
import cats.FlatMap.ops._
import cats.MonoidK.ops.toAllMonoidKOps
import com.froedevrolijk.api.db.command.CommandService
import com.froedevrolijk.api.session.RunSession

trait Combinator[F[_]] {

  def cityService: CityService[F]

  def countryService: CountryService[F]

  def healthRouteApi: HttpRoutes[F] //HealthRoute[F]

  def cityRoutes: HttpRoutes[F]

  def countryRoute: HttpRoutes[F] // CountryRoutes[F]

  def commandService: CommandService[F]

  def commandRoute: HttpRoutes[F]

  def apiRoutesCombinator: HttpRoutes[F]

}

object Combinator {

//  private val serverConfigStore = ConfigFactory.load().getConfig("store").as[ServerStoreConfig]
//  private val appConfig         = ConfigFactory.load().getConfig("api").as[AppConfig]

  def impl[F[_]: Async: ContextShift: Timer](implicit S: Session[F], B: Bracket[F, Throwable]): Combinator[F] =
    new Combinator[F] {
      // def impl // implicit session

      override def cityService: CityService[F] =
        CityService.impl[F]

      override def countryService: CountryService[F] =
        CountryService.impl[F]

      override def healthRouteApi: HttpRoutes[F] =
        HealthRoute.impl[F].healthRoute

      override def cityRoutes: HttpRoutes[F] =
        CityRoutes.impl[F](cityService).getCities

      override def countryRoute: HttpRoutes[F] =
        CountryRoutes.impl[F](countryService).getCountries

      override def commandService: CommandService[F] =
        CommandService.impl[F]

      override def commandRoute: HttpRoutes[F] =
        CommandRoutes.impl[F](commandService).insertCity

      override def apiRoutesCombinator: HttpRoutes[F] = {
        val routes = List(healthRouteApi, countryRoute, cityRoutes, commandRoute)
        routes.foldLeft(HttpRoutes.empty[F])(_ <+> _)
      }
    }

}
