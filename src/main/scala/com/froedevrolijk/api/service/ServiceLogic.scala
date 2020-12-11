package com.froedevrolijk.api.service

import com.froedevrolijk.api.db.datamodels.City

trait ServiceLogic[F[_]] extends MonadTransformers[F] {

//  def checkIfCityExists(c: String): DBResult[City]

  def checkCanBeUpdated(c: City): DBResult[City]

  def updateCityOnDb: DBResult[City]

  def checkCanBeDeleted(id: Int): Boolean
}

object ServiceLogic {

  def impl[F[_]]: ServiceLogic[F] =
    new ServiceLogic[F] {
//      override def checkIfCityExists(c: String): DBResult[City] =
//        if (selectAllCityNames(CityName("Haarlem")) c == "Haarlem")

      override def checkCanBeUpdated(c: City): DBResult[City] = ???

      override def updateCityOnDb: DBResult[City] = ???

      override def checkCanBeDeleted(id: Int): Boolean = ???
    }
}
