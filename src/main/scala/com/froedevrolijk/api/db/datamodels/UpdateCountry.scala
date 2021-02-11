package com.froedevrolijk.api.db.datamodels

import java.util.UUID

case class UpdateCountry(
    name: String,
    continent: String,
    region: String,
    surfaceArea: Double,
    independenceYear: Short,
    population: Int,
//    lifeExpectancy: Double,
    gnp: Double,
    governmentForm: String,
    headOfState: String,
    code: String
)

case class UpdateCountryMinor(
    name: String,
    continent: String,
    region: String,
    surfaceArea: Double,
    independenceYear: Short,
    population: Int,
    //    lifeExpectancy: Double,
    gnp: Double,
    governmentForm: String,
    headOfState: String
)
