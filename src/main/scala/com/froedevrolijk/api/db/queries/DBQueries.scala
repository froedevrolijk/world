package com.froedevrolijk.api.db.queries

import com.froedevrolijk.api.db.datamodels.{ City, CityName, Country, SmallCountry }
import skunk.codec.all.{ bpchar, numeric, text, varchar, _ }
import skunk.implicits._
import skunk.{ Query, Void }

object DBQueries {

  val citiesStmt: Query[String, CityName] =
    sql"""
    SELECT  name
    FROM    city
    WHERE   countrycode like $text
    LIMIT   10
    """
      .query(varchar)
      .gmap[CityName]

  val countriesStmt: Query[String, SmallCountry] =
    sql"""
      SELECT name, code, population
      FROM   country
      WHERE  name like $text
    """
      .query(varchar ~ bpchar(3) ~ int4)
      .gmap[SmallCountry]

  val selectAllCityNamesStmt: Query[String, CityName] =
    sql"""
    SELECT name 
    FROM city
    WHERE name=$varchar
    """
      .query(varchar)
      .gmap[CityName]

  val selectAllCitiesStmt: Query[Void, City] =
    sql"""
    SELECT id, name, countrycode, district, population
    FROM city
    """
      .query(int4 ~ varchar ~ bpchar(3) ~ varchar ~ int4)
      .gmap[City]

  val selectAllCountriesStmt: Query[Void, Country] =
    sql"""
      SELECT code, name, continent, region, surfacearea, COALESCE(indepyear, 0) as indepyear,
       population, gnp, governmentform, COALESCE(headofstate, 'unknown') as headofstate
      FROM   country
    """
      .query(
        bpchar(3) ~ varchar ~ varchar ~ varchar ~ numeric(10, 2) ~ int4
        ~ int4 ~ numeric(10, 2) ~ varchar ~ varchar
      )
      .gmap[Country]
}
