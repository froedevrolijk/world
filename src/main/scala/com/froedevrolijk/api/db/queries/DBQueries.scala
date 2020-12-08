package com.froedevrolijk.api.db.queries

import com.froedevrolijk.api.db.datamodels.{ City, CityName, Country }
import skunk.{ Query, Void }
import skunk.codec.all.{ bpchar, text, varchar, _ }
import skunk.implicits._

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

  val countriesStmt: Query[String, Country] =
    sql"""
      SELECT name, code, population
      FROM   country
      WHERE  name like $text
    """
      .query(varchar ~ bpchar(3) ~ int4)
      .gmap[Country]

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
    SELECT name, code, population
    FROM country
    """
      .query(varchar ~ varchar ~ int4)
      .gmap[Country]
}
