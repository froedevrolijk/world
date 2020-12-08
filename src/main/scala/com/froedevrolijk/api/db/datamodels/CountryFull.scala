package com.froedevrolijk.api.db.datamodels

case class CountryFull(
    code: String,
    name: String,
    continent: String,
    region: String,
    surfaceArea: Double,
    independenceYear: Short,
    population: Int,
    lifeExpectancy: Double,
    gnp: Double,
    governmentForm: String,
    headOfState: String
)
