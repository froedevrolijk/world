package com.froedevrolijk.api.db.sqlstatements

import java.util.UUID

import com.froedevrolijk.api.db.datamodels.{ City, UpdateCity, UpdateCountry }
import skunk.Command
import skunk.codec.all.{ bpchar, varchar, _ }
import skunk.implicits._

object Commands {

  val insertSingleCityStmt: Command[City] =
    sql"""
    INSERT INTO city(name, countrycode, district, population)
    VALUES ($varchar, $bpchar, $varchar, $int4)
    """.command.gcontramap[City]

  val updateCityStmt: Command[UpdateCity] =
    sql"""
    UPDATE city
    SET name = $varchar,
        countrycode = ${bpchar(3)},
        district = $varchar,
        population = $int4
    WHERE cityid = $uuid
    """.command
      .gcontramap[UpdateCity]

  val updateCountryStmt: Command[UpdateCountry] =
    sql"""
    UPDATE country
    SET name = $varchar,
        continent = $varchar,
        region = $varchar,
        surfacearea = $float4,
        indepyear = $int2,
        population = $int4,
        lifeexpectancy = $float4,
        gnp = $float4,
        governmentform = $varchar,
        headofstate = $varchar
    WHERE code = ${bpchar(3)}
    """.command
      .gcontramap[UpdateCountry]

  val deleteCityStmt: Command[UUID] =
    sql"""
    DELETE FROM city
    WHERE cityid = $uuid
    """.command
  //  WHERE EXISTS (SELECT 1 FROM city WHERE id = $int4)

  val deleteCountryStmt: Command[UUID] =
    sql"""
    DELETE FROM country
    WHERE countryid = $uuid
    """.command
}