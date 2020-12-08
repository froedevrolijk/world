package com.froedevrolijk.api.db.datamodels

case class UpdateCountry(
    name: String,
    continent: String,
    region: String,
    surfaceArea: Double,
    independenceYear: Short,
    population: Int,
    lifeExpectancy: Double,
    gnp: Double,
    governmentForm: String,
    headOfState: String,
    code: String
)
