import cats.effect.IO
import com.froedevrolijk.api.db.datamodels.CityName
import com.froedevrolijk.api.exception.CityNotFoundException

val cityName: List[CityName] = List(CityName("Bloemendaal"))
val emptyCityName: List[CityName] = List()
val fListCityName: IO[List[CityName]] = IO.apply(emptyCityName)

cityName match {
  case Nil => CityNotFoundException("city was not found")
  case _ => "city was found"
}

def matcher(cities: List[CityName]): Either[Throwable, List[CityName]] =
  cities match {
    case Nil => Left(CityNotFoundException("city not found"))
    case x => Right(x)
  }

matcher(emptyCityName)