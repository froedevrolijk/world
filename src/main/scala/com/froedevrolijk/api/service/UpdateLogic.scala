package com.froedevrolijk.api.service

import com.froedevrolijk.api.db.datamodels.City

trait UpdateLogic[F[_]] extends MonadTransformers[F] {

//  def checkIfCityExists(c: String): DBResult[City]

  def checkCanBeUpdated(c: City): DBResult[City]

  def updateCityOnDb: DBResult[City]
}

object UpdateLogic {

  def impl[F[_]]: UpdateLogic[F] =
    new UpdateLogic[F] {
//      override def checkIfCityExists(c: String): DBResult[City] =
//        if (selectAllCityNames(CityName("Haarlem")) c == "Haarlem")

      override def checkCanBeUpdated(c: City): DBResult[City] = ???

      override def updateCityOnDb: DBResult[City] = ???
    }
}
