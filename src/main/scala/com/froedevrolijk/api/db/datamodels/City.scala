package com.froedevrolijk.api.db.datamodels

import java.util.UUID

case class City(name: String, countryCode: String, district: String, population: Int)
case class UpdateCity(name: String, countryCode: String, district: String, population: Int, cityid: UUID)
