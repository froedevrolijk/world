package com.froedevrolijk.api.db.command

import com.froedevrolijk.api.db.datamodels.{ City, CityName, UpdateCity, UpdateCityPopulation, UpdateCountryPopulation }
import skunk.codec.all.{ varchar, _ }
import skunk.{ Command, Query, Void }
import cats.effect._
import skunk._
import skunk.implicits._
import skunk.codec.all._
import java.time.OffsetDateTime

import natchez.Trace.Implicits.noop
import fs2.Stream
import cats.Applicative

object DBCommands {

  val insertSingleCityStmt: Command[City] =
    sql"""
    INSERT INTO city 
    VALUES ($int4, $varchar, $bpchar, $varchar, $int4)
    """.command.gcontramap[City]

//  def insertMultipleCitiesStmt(ps: List[City]): Command[ps.type] = {
//    val enc = (int4 ~ varchar ~ bpchar(3) ~ varchar ~ int4).gcontramap[City].values.list(ps)
//    sql"""
//    INSERT INTO city
//    VALUES $enc
//    """.command
//  }

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

  val updateCountryStmt: Command[UpdateCountryPopulation] =
    sql"""
    UPDATE country
    SET population = $int4
    WHERE name = $varchar
    """.command
      .gcontramap[UpdateCountryPopulation]

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

//  def deleteManyStmt(n: Int): Command[List[String]] =
//    sql"""
//    DELETE FROM country
//    WHERE name IN (${varchar.list(n)})
//    """.command
}
