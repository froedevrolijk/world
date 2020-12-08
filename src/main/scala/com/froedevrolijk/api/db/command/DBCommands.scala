package com.froedevrolijk.api.db.command

import com.froedevrolijk.api.db.datamodels.{ City, UpdateCity, UpdateCountry }
import skunk.Command
import skunk.codec.all.{ varchar, _ }
import skunk.implicits._

object DBCommands {

  val insertSingleCityStmt: Command[City] =
    sql"""
    INSERT INTO city 
    VALUES ($int4, $varchar, $bpchar, $varchar, $int4)
    """.command.gcontramap[City]

  val updateCityStmt: Command[UpdateCity] =
    sql"""
    UPDATE city
    SET name = $varchar,
        countrycode = ${bpchar(3)},
        district = $varchar,
        population = $int4
    WHERE id = $int4
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

  val deleteSingleCityStmt: Command[Int] =
    sql"""
    DELETE FROM city
    WHERE id = $int4
    """.command

  val deleteSingleCountryStmt: Command[String] =
    sql"""
    DELETE FROM country
    WHERE code = ${bpchar(3)}
    """.command
}
