package com.froedevrolijk.api.db.datamodels

case class City(id: Int, name: String, countryCode: String, district: String, population: Int)
case class UpdateCity(name: String, countryCode: String, district: String, population: Int, id: Int)
