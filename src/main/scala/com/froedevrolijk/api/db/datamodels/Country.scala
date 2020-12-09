package com.froedevrolijk.api.db.datamodels

case class Country(
    code: String,
    name: String,
    continent: String,
    region: String,
    surfaceArea: BigDecimal,
    independenceYear: Int,
    population: Int,
    gnp: BigDecimal,
    governmentForm: String,
    headOfState: String
)
